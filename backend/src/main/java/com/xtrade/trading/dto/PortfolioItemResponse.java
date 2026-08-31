package com.xtrade.trading.dto;

import com.xtrade.trading.model.PortfolioItem;

/**
 * Ligne du portefeuille exposée par l'API.
 */
public record PortfolioItemResponse(
        Long assetId,
        String code,
        String nom,
        String type,
        double prixUnitaire,
        int quantite,
        double valeur
) {
    public static PortfolioItemResponse from(PortfolioItem item) {
        return new PortfolioItemResponse(
                item.getAsset().getId(),
                item.getAsset().getCode(),
                item.getAsset().getNom(),
                item.getAsset().getDiscriminator(),
                item.getAsset().getPrixUnitaire(),
                item.getQuantite(),
                item.getValeur()
        );
    }
}
