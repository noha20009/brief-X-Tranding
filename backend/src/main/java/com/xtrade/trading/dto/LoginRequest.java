package com.xtrade.trading.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Corps de requête pour la connexion d'un utilisateur.
 */
public record LoginRequest(
        @NotBlank(message = "L'email est obligatoire")
        String email,

        @NotBlank(message = "Le mot de passe est obligatoire")
        String password
) {}