package com.xtrade.trading.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Corps de requête pour la création d'un compte.
 */
public record RegisterRequest(
        @NotBlank(message = "Le nom est obligatoire")
        String nom,

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "Email invalide")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
        String password,

        @NotNull(message = "Le solde est obligatoire")
        @DecimalMin(value = "0.0", message = "Le solde ne peut pas être négatif")
        Double balance
) {}