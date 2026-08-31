package com.xtrade.trading.dto;

import com.xtrade.trading.model.Trader;

/**
 * Réponse d'un trader pour l'API.
 */
public record TraderResponse(
        Long id,
        String nom,
        double balance,
        double valeurPortefeuille,
        double valeurTotale
) {
    public static TraderResponse from(Trader trader, double valeurPortefeuille) {
        return new TraderResponse(
                trader.getId(),
                trader.getNom(),
                trader.getBalance(),
                valeurPortefeuille,
                trader.getBalance() + valeurPortefeuille
        );
    }
}
