package com.xtrade.trading.dto;

import java.util.List;

/**
 * Réponse du portefeuille d'un trader : détails des actifs détenus + valeur totale.
 */
public record PortfolioResponse(
        Long traderId,
        String traderNom,
        double solde,
        double valeurPortefeuille,
        double valeurTotale,
        List<PortfolioItemResponse> actifs
) {}
