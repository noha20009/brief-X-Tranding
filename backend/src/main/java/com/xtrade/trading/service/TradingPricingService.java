package com.xtrade.trading.service;

import com.xtrade.trading.model.Asset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Applique le spread et les frais de transaction sur le cours des actifs.
 * Les frais sont exprimés en pourcentage du montant brut.
 */
@Service
public class TradingPricingService {

    private static final double STOCK_SPREAD = 0.001;   // 0,10 %
    private static final double CRYPTO_SPREAD = 0.0025; // 0,25 %

    private final RealtimeQuoteService quoteService;

    private final double feeRate;

    public TradingPricingService(
            RealtimeQuoteService quoteService,
            @Value("${app.trading.fee-rate:0.001}") double feeRate) {
        this.quoteService = quoteService;
        this.feeRate = feeRate;
    }

    public double getFeeRate() {
        return feeRate;
    }

    /** Prix de marché actuel de l'actif (avec repli sur le prix en base). */
    public double getLivePrice(Asset asset) {
        return quoteService.getCurrentPrice(asset.getId(), asset.getPrixUnitaire());
    }

    /** Prix d'achat effectif (cours + moitié du spread). */
    public double getBuyPrice(Asset asset) {
        return round(getLivePrice(asset) * (1 + spread(asset) / 2));
    }

    /** Prix de vente effectif (cours - moitié du spread). */
    public double getSellPrice(Asset asset) {
        return round(getLivePrice(asset) * (1 - spread(asset) / 2));
    }

    /** Montant des frais pour un montant brut donné. */
    public double feeFor(double gross) {
        return round(gross * feeRate);
    }

    private double spread(Asset asset) {
        return "CRYPTO".equals(asset.getDiscriminator()) ? CRYPTO_SPREAD : STOCK_SPREAD;
    }

    private static double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
