package com.xtrade.trading.dto;

import com.xtrade.trading.model.Asset;

/**
 * Réponse d'un actif pour l'API.
 */
public record AssetResponse(
        Long id,
        String code,
        String nom,
        double prixUnitaire,
        String type
) {
    public static AssetResponse from(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getCode(),
                asset.getNom(),
                asset.getPrixUnitaire(),
                asset.getDiscriminator()
        );
    }
}
