package com.xtrade.trading.service;

import com.xtrade.trading.dto.OrderRequest;
import com.xtrade.trading.dto.OrderResponse;
import com.xtrade.trading.dto.TradeRequest;
import com.xtrade.trading.exception.TradingException;
import com.xtrade.trading.model.*;
import com.xtrade.trading.repository.AssetRepository;
import com.xtrade.trading.repository.OrderRepository;
import com.xtrade.trading.repository.PortfolioItemRepository;
import com.xtrade.trading.repository.TraderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gère les ordres conditionnels (limite / stop-loss / take-profit).
 * Les ordres en attente sont vérifiés périodiquement contre le cours temps réel
 * et exécutés automatiquement lorsque leur prix déclencheur est franchi.
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final long CHECK_MS = 2000;

    private final OrderRepository orderRepository;
    private final TraderRepository traderRepository;
    private final AssetRepository assetRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    private final MarketService marketService;
    private final RealtimeQuoteService quoteService;

    public OrderService(OrderRepository orderRepository,
                        TraderRepository traderRepository,
                        AssetRepository assetRepository,
                        PortfolioItemRepository portfolioItemRepository,
                        MarketService marketService,
                        RealtimeQuoteService quoteService) {
        this.orderRepository = orderRepository;
        this.traderRepository = traderRepository;
        this.assetRepository = assetRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.marketService = marketService;
        this.quoteService = quoteService;
    }

    // ==================== PLACEMENT ====================

    @Transactional
    public OrderResponse placeOrder(OrderRequest request) {
        Trader trader = findTrader(request.traderId());
        Asset asset = findAsset(request.assetId());
        int quantite = request.quantite();
        double prix = request.prixDeclaration();
        OrderType type = request.type();
        OrderDirection dir = request.direction();

        if (prix <= 0) {
            throw new TradingException("Le prix déclencheur doit être strictement positif.");
        }

        // Règles métier par type d'ordre
        if ((type == OrderType.STOP_LOSS || type == OrderType.TAKE_PROFIT) && dir != OrderDirection.SELL) {
            throw new TradingException("Un ordre STOP_LOSS / TAKE_PROFIT doit être une vente (direction SELL).");
        }
        if (type == OrderType.LIMIT && dir == OrderDirection.BUY) {
            double live = quoteService.getCurrentPrice(asset.getId(), asset.getPrixUnitaire());
            if (prix >= live) {
                throw new TradingException("Un ordre d'achat limit doit être placé sous le cours actuel ("
                        + Math.round(live * 100) / 100.0 + ").");
            }
        }
        if (dir == OrderDirection.SELL) {
            ensureHolding(trader, asset, quantite);
        }

        Order order = orderRepository.save(new Order(trader, asset, type, dir, quantite, prix));
        return OrderResponse.from(order);
    }

    // ==================== LISTE / ANNULATION ====================

    @Transactional(readOnly = true)
    public List<OrderResponse> listOrders(Long traderId) {
        findTrader(traderId);
        return orderRepository.findByTraderIdOrderByDatePlacementDesc(traderId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> listPendingOrders(Long traderId) {
        findTrader(traderId);
        return orderRepository.findByTraderIdAndStatutOrderByDatePlacementDesc(traderId, OrderStatus.PENDING).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional
    public OrderResponse cancelOrder(Long traderId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new TradingException("Ordre introuvable avec l'id " + orderId));
        if (!order.getTrader().getId().equals(traderId)) {
            throw new TradingException("Cet ordre n'appartient pas au trader demandé.");
        }
        if (order.getStatut() != OrderStatus.PENDING) {
            throw new TradingException("Seuls les ordres en attente peuvent être annulés.");
        }
        order.setStatut(OrderStatus.CANCELLED);
        order.setDateExecution(LocalDateTime.now());
        return OrderResponse.from(orderRepository.save(order));
    }

    // ==================== EXECUTION AUTOMATIQUE ====================

    @Scheduled(fixedDelay = CHECK_MS)
    @Transactional
    public void processPendingOrders() {
        List<Order> pending = orderRepository.findByStatut(OrderStatus.PENDING);
        if (pending.isEmpty()) {
            return;
        }
        for (Order order : pending) {
            double live = quoteService.getCurrentPrice(order.getAsset().getId(), order.getAsset().getPrixUnitaire());
            if (!isTriggered(order, live)) {
                continue;
            }
            execute(order);
        }
    }

    private boolean isTriggered(Order order, double live) {
        return switch (order.getType()) {
            case LIMIT -> order.getDirection() == OrderDirection.BUY
                    ? live <= order.getPrixDeclaration()
                    : live >= order.getPrixDeclaration();
            case STOP_LOSS -> live <= order.getPrixDeclaration();
            case TAKE_PROFIT -> live >= order.getPrixDeclaration();
        };
    }

    private void execute(Order order) {
        try {
            TradeRequest trade = new TradeRequest(
                    order.getTrader().getId(),
                    order.getAsset().getId(),
                    order.getQuantite()
            );
            if (order.getDirection() == OrderDirection.BUY) {
                marketService.buyAsset(trade);
            } else {
                marketService.sellAsset(trade);
            }
            order.setStatut(OrderStatus.EXECUTED);
            order.setDateExecution(LocalDateTime.now());
            orderRepository.save(order);
            log.info("Ordre {} exécuté pour le trader {} ({} {} x{})",
                    order.getId(), order.getTrader().getNom(),
                    order.getDirection(), order.getAsset().getCode(), order.getQuantite());
        } catch (RuntimeException ex) {
            order.setStatut(OrderStatus.CANCELLED);
            order.setDateExecution(LocalDateTime.now());
            orderRepository.save(order);
            log.warn("Ordre {} annulé après échec d'exécution : {}", order.getId(), ex.getMessage());
        }
    }

    // ==================== HELPERS ====================

    private void ensureHolding(Trader trader, Asset asset, int quantite) {
        PortfolioItem item = portfolioItemRepository.findByTraderIdAndAssetId(trader.getId(), asset.getId()).orElse(null);
        int held = item != null ? item.getQuantite() : 0;
        if (held < quantite) {
            throw new TradingException("Quantité insuffisante pour l'ordre de vente : vous détenez "
                    + held + " unités de " + asset.getCode() + ".");
        }
    }

    private Trader findTrader(Long id) {
        return traderRepository.findById(id)
                .orElseThrow(() -> new TradingException("Trader introuvable avec l'id " + id));
    }

    private Asset findAsset(Long id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new TradingException("Actif introuvable avec l'id " + id));
    }
}
