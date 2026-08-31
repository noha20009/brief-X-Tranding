package com.xtrade.trading.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

/**
 * Corps de requête pour l'ajout d'un actif (stock ou crypto).
 */
public record AssetRequest(
        @NotBlank(message = "Le code est obligatoire")
        String code,

        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être strictement positif")
        double prixUnitaire,

        @NotBlank(message = "Le type est obligatoire (STOCK ou CRYPTO)")
        String type
) {}
