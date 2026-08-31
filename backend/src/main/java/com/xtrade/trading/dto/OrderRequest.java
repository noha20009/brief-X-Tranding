package com.xtrade.trading.dto;

import com.xtrade.trading.model.OrderDirection;
import com.xtrade.trading.model.OrderType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Corps de requête pour placer un ordre conditionnel
 * (limite / stop-loss / take-profit).
 */
public record OrderRequest(
        @NotNull(message = "L'identifiant du trader est obligatoire")
        Long traderId,

        @NotNull(message = "L'identifiant de l'actif est obligatoire")
        Long assetId,

        @NotNull(message = "Le type d'ordre est obligatoire")
        OrderType type,

        @NotNull(message = "La direction est obligatoire")
        OrderDirection direction,

        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être strictement positive")
        Integer quantite,

        @NotNull(message = "Le prix déclencheur est obligatoire")
        @Min(value = 1, message = "Le prix doit être strictement positif")
        Double prixDeclaration
) {}
