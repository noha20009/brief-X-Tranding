package com.xtrade.trading.dto;

import java.util.List;

/**
 * Réponse de la performance d'un trader : gains/pertes réalisés
 * (par transactions) et gains/pertes latents (sur le portefeuille actuel).
 */
public record PerformanceResponse(
        Long traderId,
        String traderNom,
        double solde,
        double capitalInvestiTotal,      // somme des achats
        double montantRecupereTotal,     // somme des ventes
        double gainsPertesRealises,      // montantRecupere - capitalInvesti
        double valeurPortefeuilleActuel, // valeur au prix courant des actifs détenus
        double gainsPertesLatents,       // valeur actuelle du portefeuille - prix d'achat des actifs détenus
        double performancePourcentage,   // (montantRecupere + valeurPortefeuille) / capitalInvesti - 1
        List<TransactionResponse> transactions
) {}
