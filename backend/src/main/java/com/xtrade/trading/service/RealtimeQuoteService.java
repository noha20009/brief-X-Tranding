package com.xtrade.trading.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xtrade.trading.dto.QuoteResponse;
import com.xtrade.trading.model.Asset;
import com.xtrade.trading.repository.AssetRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Simule l'évolution des cours en temps réel et diffuse les cotations
 * à tous les clients WebSocket connectés sur /ws/quotes.
 */
@Service
@EnableScheduling
public class RealtimeQuoteService {

    private static final Logger log = LoggerFactory.getLogger(RealtimeQuoteService.class);

    private static final double VOLATILITY = 0.004; // +/- 0.4% par tick
    private static final long TICK_MS = 2000;       // un tick toutes les 2 s

    private final AssetRepository assetRepository;
    private final ObjectMapper objectMapper;

    // prixCourant et prixOuverture par asset
    private final Map<Long, double[]> prices = new ConcurrentHashMap<>();

    // sessions WebSocket connectées
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public RealtimeQuoteService(AssetRepository assetRepository, ObjectMapper objectMapper) {
        this.assetRepository = assetRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    void seedPrices() {
        for (Asset asset : assetRepository.findAll()) {
            double p = asset.getPrixUnitaire();
            prices.put(asset.getId(), new double[]{p, p});
        }
        if (prices.isEmpty()) {
            log.warn("Aucun actif détecté au démarrage : la simulation de cotation est inactive.");
        } else {
            log.info("Cotations temps réel initialisées pour {} actifs.", prices.size());
        }
    }

    public double getCurrentPrice(Long assetId, double fallback) {
        double[] p = prices.get(assetId);
        return p != null ? p[0] : fallback;
    }

    public void register(WebSocketSession session) {
        sessions.add(session);
        send(session, snapshot());
    }

    public void unregister(WebSocketSession session) {
        sessions.remove(session);
    }

    @Scheduled(fixedDelay = TICK_MS)
    void tick() {
        if (prices.isEmpty() || sessions.isEmpty()) {
            return;
        }
        for (double[] p : prices.values()) {
            double drift = 1.0 + ThreadLocalRandom.current().nextGaussian() * VOLATILITY;
            p[0] = Math.max(0.01, p[0] * drift);
        }
        broadcast(snapshot());
    }

    private List<QuoteResponse> snapshot() {
        return prices.entrySet().stream()
                .map(e -> {
                    double current = e.getValue()[0];
                    double open = e.getValue()[1];
                    double variation = open > 0 ? (current - open) / open * 100 : 0.0;
                    Asset asset = assetRepository.findById(e.getKey()).orElse(null);
                    if (asset == null) {
                        return null;
                    }
                    return new QuoteResponse(
                            asset.getId(),
                            asset.getCode(),
                            asset.getNom(),
                            asset.getDiscriminator(),
                            current,
                            variation
                    );
                })
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    private void broadcast(List<QuoteResponse> quotes) {
        String payload = toJson(quotes);
        for (WebSocketSession session : sessions) {
            send(session, payload);
        }
    }

    private void send(WebSocketSession session, List<QuoteResponse> quotes) {
        send(session, toJson(quotes));
    }

    private void send(WebSocketSession session, String payload) {
        if (!session.isOpen()) {
            sessions.remove(session);
            return;
        }
        try {
            session.sendMessage(new TextMessage(payload));
        } catch (IOException e) {
            log.warn("Échec d'envoi WebSocket : {}", e.getMessage());
            try {
                session.close();
            } catch (IOException ignored) {
                // rien à faire
            }
            sessions.remove(session);
        }
    }

    private String toJson(List<QuoteResponse> quotes) {
        try {
            return objectMapper.writeValueAsString(quotes);
        } catch (JsonProcessingException e) {
            log.error("Erreur de sérialisation des cotations", e);
            return "[]";
        }
    }
}
