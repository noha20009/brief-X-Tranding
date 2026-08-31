package com.xtrade.trading.dto;

import com.xtrade.trading.model.Transaction;
import com.xtrade.trading.model.TransactionType;

import java.time.LocalDateTime;

/**
 * Réponse d'une transaction pour l'API.
 */
public record TransactionResponse(
        Long id,
        TransactionType type,
        Long traderId,
        String traderNom,
        Long assetId,
        String assetCode,
        String assetNom,
        int quantite,
        double prixUnitaire,
        double montantTotal,
        double frais,
        LocalDateTime date
) {
    public static TransactionResponse from(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getType(),
                t.getTrader().getId(),
                t.getTrader().getNom(),
                t.getAsset().getId(),
                t.getAsset().getCode(),
                t.getAsset().getNom(),
                t.getQuantite(),
                t.getPrixUnitaire(),
                t.getMontantTotal(),
                t.getFrais() == null ? 0.0 : t.getFrais(),
                t.getDate()
        );
    }
}
