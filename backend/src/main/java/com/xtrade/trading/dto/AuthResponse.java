package com.xtrade.trading.dto;

import com.xtrade.trading.model.Trader;

/**
 * Réponse d'authentification : jeton JWT + informations du trader connecté.
 */
public record AuthResponse(
        String token,
        String type,
        Long traderId,
        String nom,
        String email,
        String role
) {
    public static AuthResponse from(Trader trader, String token) {
        return new AuthResponse(
                token,
                "Bearer",
                trader.getId(),
                trader.getNom(),
                trader.getEmail(),
                trader.getRole()
        );
    }
}