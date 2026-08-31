package com.xtrade.trading.dto;

/**
 * Cotation temps réel d'un actif poussée via WebSocket.
 */
public record QuoteResponse(
        Long assetId,
        String code,
        String nom,
        String type,
        double prix,
        double variationPct
) {
}
