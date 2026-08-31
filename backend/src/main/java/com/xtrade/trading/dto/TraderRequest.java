package com.xtrade.trading.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Corps de requête pour la création d'un trader.
 */
public record TraderRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotNull(message = "Le solde est obligatoire")
        @DecimalMin(value = "0.0", message = "Le solde ne peut pas être négatif")
        Double balance
) {}
