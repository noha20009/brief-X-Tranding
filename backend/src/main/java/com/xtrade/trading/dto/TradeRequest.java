package com.xtrade.trading.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Corps de requête pour une opération d'achat ou de vente.
 */
public record TradeRequest(
        @NotNull(message = "L'identifiant du trader est obligatoire")
        Long traderId,

        @NotNull(message = "L'identifiant de l'actif est obligatoire")
        Long assetId,

        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être strictement positive")
        Integer quantite
) {}
