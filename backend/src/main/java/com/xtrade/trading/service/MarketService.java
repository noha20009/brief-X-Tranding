package com.xtrade.trading.service;

import com.xtrade.trading.dto.*;
import com.xtrade.trading.exception.TradingException;
import com.xtrade.trading.model.*;
import com.xtrade.trading.repository.AssetRepository;
import com.xtrade.trading.repository.PortfolioItemRepository;
import com.xtrade.trading.repository.TraderRepository;
import com.xtrade.trading.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Centralise les opérations métier du système de trading.
 * (Équivalent fonctionnel de la classe Market du projet console.)
 */
@Service
@Transactional
public class MarketService {

    private final TraderRepository traderRepository;
    private final AssetRepository assetRepository;
    private final PortfolioItemRepository portfolioItemRepository;
    private final TransactionRepository transactionRepository;
    private final TradingPricingService pricingService;

    public MarketService(TraderRepository traderRepository,
                         AssetRepository assetRepository,
                         PortfolioItemRepository portfolioItemRepository,
                         TransactionRepository transactionRepository,
                         TradingPricingService pricingService) {
        this.traderRepository = traderRepository;
        this.assetRepository = assetRepository;
        this.portfolioItemRepository = portfolioItemRepository;
        this.transactionRepository = transactionRepository;
        this.pricingService = pricingService;
    }

    // ==================== TRADERS ====================

    public TraderResponse createTrader(TraderRequest request) {
        if (request.balance() < 0) {
            throw new TradingException("Le solde initial ne peut pas être négatif.");
        }
        Trader trader = new Trader(request.nom().trim(), request.balance());
        trader = traderRepository.save(trader);
        return TraderResponse.from(trader, 0.0);
    }

    @Transactional(readOnly = true)
    public List<TraderResponse> listTraders() {
        return traderRepository.findAll().stream()
                .map(t -> TraderResponse.from(t, getPortfolioValue(t.getId())))
                .toList();
    }

    @Transactional(readOnly = true)
    public TraderResponse getTrader(Long id) {
        Trader trader = findTrader(id);
        return TraderResponse.from(trader, getPortfolioValue(id));
    }

    // ==================== ASSETS ====================

    public AssetResponse createAsset(AssetRequest request) {
        if (request.prixUnitaire() <= 0) {
            throw new TradingException("Le prix unitaire doit être strictement positif.");
        }
        if (assetRepository.existsByCode(request.code().trim().toUpperCase())) {
            throw new TradingException("Le code de l'actif '" + request.code() + "' existe déjà.");
        }

        Asset asset;
        String type = request.type().trim().toUpperCase();
        switch (type) {
            case "STOCK" -> asset = new Stock(request.code().trim().toUpperCase(), request.nom().trim(), request.prixUnitaire());
            case "CRYPTO" -> asset = new CryptoCurrency(request.code().trim().toUpperCase(), request.nom().trim(), request.prixUnitaire());
            default -> throw new TradingException("Type d'actif invalide : " + type + " (attendu STOCK ou CRYPTO).");
        }
        return AssetResponse.from(assetRepository.save(asset));
    }

    @Transactional(readOnly = true)
    public List<AssetResponse> listAssets() {
        return assetRepository.findAll().stream().map(AssetResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public AssetResponse getAsset(Long id) {
        return AssetResponse.from(findAsset(id));
    }

    public AssetResponse updateAssetPrice(Long id, double nouveauPrix) {
        if (nouveauPrix <= 0) {
            throw new TradingException("Le prix unitaire doit être strictement positif.");
        }
        Asset asset = findAsset(id);
        asset.setPrixUnitaire(nouveauPrix);
        return AssetResponse.from(assetRepository.save(asset));
    }

    // ==================== TRADING (ACHAT / VENTE) ====================

    public TransactionResponse buyAsset(TradeRequest request) {
        Trader trader = findTrader(request.traderId());
        Asset asset = findAsset(request.assetId());
        int quantite = request.quantite();

        if (quantite <= 0) {
            throw new TradingException("La quantité doit être strictement positive.");
        }
        double prixUnitaire = pricingService.getBuyPrice(asset);
        double gross = prixUnitaire * quantite;
        double frais = pricingService.feeFor(gross);
        double totalDebit = gross + frais;
        if (trader.getBalance() < totalDebit) {
            throw new TradingException("Solde insuffisant : besoin de " + format(totalDebit)
                    + " (incl. " + format(frais) + " de frais) mais solde = " + format(trader.getBalance()));
        }

        trader.updateBalance(-totalDebit);

        // Mise à jour / création de la ligne du portefeuille
        PortfolioItem item = portfolioItemRepository
                .findByTraderIdAndAssetId(trader.getId(), asset.getId())
                .orElse(null);
        if (item == null) {
            item = new PortfolioItem(trader, asset, quantite);
        } else {
            item.setQuantite(item.getQuantite() + quantite);
        }
        portfolioItemRepository.save(item);

        Transaction tx = new Transaction(TransactionType.ACHAT, trader, asset, quantite, prixUnitaire, frais);
        tx = transactionRepository.save(tx);
        trader.getTransactions().add(tx);
        traderRepository.save(trader);

        return TransactionResponse.from(tx);
    }

    public TransactionResponse sellAsset(TradeRequest request) {
        Trader trader = findTrader(request.traderId());
        Asset asset = findAsset(request.assetId());
        int quantite = request.quantite();

        if (quantite <= 0) {
            throw new TradingException("La quantité doit être strictement positive.");
        }

        PortfolioItem item = portfolioItemRepository
                .findByTraderIdAndAssetId(trader.getId(), asset.getId())
                .orElseThrow(() -> new TradingException("L'actif '" + asset.getNom() + "' n'est pas détenu dans le portefeuille."));

        if (item.getQuantite() < quantite) {
            throw new TradingException("Quantité insuffisante : vous détenez " + item.getQuantite() + " unités.");
        }

        double prixUnitaire = pricingService.getSellPrice(asset);
        double gross = prixUnitaire * quantite;
        double frais = pricingService.feeFor(gross);
        double revenue = gross - frais;
        trader.updateBalance(revenue);

        if (item.getQuantite() == quantite) {
            portfolioItemRepository.delete(item);
        } else {
            item.setQuantite(item.getQuantite() - quantite);
            portfolioItemRepository.save(item);
        }

        Transaction tx = new Transaction(TransactionType.VENTE, trader, asset, quantite, prixUnitaire, frais);
        tx = transactionRepository.save(tx);
        trader.getTransactions().add(tx);
        traderRepository.save(trader);

        return TransactionResponse.from(tx);
    }

    // ==================== PORTEFEUILLE ====================

    @Transactional(readOnly = true)
    public double getPortfolioValue(Long traderId) {
        return portfolioItemRepository.findByTraderId(traderId).stream()
                .mapToDouble(PortfolioItem::getValeur)
                .sum();
    }

    @Transactional(readOnly = true)
    public PortfolioResponse getPortfolio(Long traderId) {
        Trader trader = findTrader(traderId);
        List<PortfolioItemResponse> items = portfolioItemRepository.findByTraderId(traderId).stream()
                .map(PortfolioItemResponse::from)
                .toList();
        double valeurPortefeuille = items.stream().mapToDouble(PortfolioItemResponse::valeur).sum();
        return new PortfolioResponse(
                trader.getId(),
                trader.getNom(),
                trader.getBalance(),
                valeurPortefeuille,
                trader.getBalance() + valeurPortefeuille,
                items
        );
    }

    // ==================== TRANSACTIONS ====================

    @Transactional(readOnly = true)
    public List<TransactionResponse> listTransactions() {
        return transactionRepository.findAllByOrderByDateAsc().stream()
                .map(TransactionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> listTransactionsForTrader(Long traderId) {
        findTrader(traderId);
        return transactionRepository.findByTraderIdOrderByDateAsc(traderId).stream()
                .map(TransactionResponse::from)
                .toList();
    }

    // ==================== STATISTIQUES / PERFORMANCE ====================

    @Transactional(readOnly = true)
    public StatsResponse getStats() {
        long traders = traderRepository.count();
        long assets = assetRepository.count();
        List<Transaction> all = transactionRepository.findAll();

        int count = all.size();
        double totalAchats = all.stream().filter(t -> t.getType() == TransactionType.ACHAT)
                .mapToDouble(Transaction::getMontantTotal).sum();
        double totalVentes = all.stream().filter(t -> t.getType() == TransactionType.VENTE)
                .mapToDouble(Transaction::getMontantTotal).sum();
        Map<String, Integer> volume = all.stream()
                .collect(Collectors.groupingBy(t -> t.getAsset().getNom(),
                        Collectors.summingInt(Transaction::getQuantite)));

        return new StatsResponse(traders, assets, count, totalAchats, totalVentes, volume);
    }

    @Transactional(readOnly = true)
    public PerformanceResponse getPerformance(Long traderId) {
        Trader trader = findTrader(traderId);
        List<Transaction> txs = transactionRepository.findByTraderIdOrderByDateAsc(traderId);

        double investi = txs.stream().filter(t -> t.getType() == TransactionType.ACHAT)
                .mapToDouble(Transaction::getMontantTotal).sum();
        double recupere = txs.stream().filter(t -> t.getType() == TransactionType.VENTE)
                .mapToDouble(Transaction::getMontantTotal).sum();
        double realized = recupere - investi;

        double valeurCourante = getPortfolioValue(traderId);
        double latent = valeurCourante - investi;

        double perfPct = investi > 0 ? ((recupere + valeurCourante) / investi - 1) * 100 : 0.0;

        return new PerformanceResponse(
                trader.getId(),
                trader.getNom(),
                trader.getBalance(),
                investi,
                recupere,
                realized,
                valeurCourante,
                latent,
                perfPct,
                txs.stream().map(TransactionResponse::from).toList()
        );
    }

    // ==================== HELPERS ====================

    private static String format(double value) {
        return String.format("%.2f", value);
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
