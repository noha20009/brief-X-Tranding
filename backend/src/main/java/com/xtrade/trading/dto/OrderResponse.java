package com.xtrade.trading.dto;

import com.xtrade.trading.model.Order;
import com.xtrade.trading.model.OrderDirection;
import com.xtrade.trading.model.OrderStatus;
import com.xtrade.trading.model.OrderType;

import java.time.LocalDateTime;

/**
 * Réponse d'un ordre pour l'API.
 */
public record OrderResponse(
        Long id,
        Long traderId,
        String traderNom,
        Long assetId,
        String assetCode,
        String assetNom,
        OrderType type,
        OrderDirection direction,
        int quantite,
        double prixDeclaration,
        OrderStatus statut,
        LocalDateTime datePlacement,
        LocalDateTime dateExecution
) {
    public static OrderResponse from(Order o) {
        return new OrderResponse(
                o.getId(),
                o.getTrader().getId(),
                o.getTrader().getNom(),
                o.getAsset().getId(),
                o.getAsset().getCode(),
                o.getAsset().getNom(),
                o.getType(),
                o.getDirection(),
                o.getQuantite(),
                o.getPrixDeclaration(),
                o.getStatut(),
                o.getDatePlacement(),
                o.getDateExecution()
        );
    }
}
