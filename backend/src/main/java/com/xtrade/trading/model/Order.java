package com.xtrade.trading.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Ordre conditionnel (limite / stop-loss / take-profit) placé par un trader.
 * L'ordre reste en attente (PENDING) tant que le cours n'a pas franchi le prix
 * déclencheur ; il est ensuite exécuté automatiquement (EXECUTED) ou annulé.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trader_id", nullable = false)
    private Trader trader;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OrderDirection direction;

    @Column(nullable = false)
    private int quantite;

    /** Prix déclencheur (limite ou stop). */
    @Column(nullable = false)
    private double prixDeclaration;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private OrderStatus statut;

    @Column(nullable = false)
    private LocalDateTime datePlacement;

    private LocalDateTime dateExecution;

    public Order(Trader trader, Asset asset, OrderType type,
                 OrderDirection direction, int quantite, double prixDeclaration) {
        this.trader = trader;
        this.asset = asset;
        this.type = type;
        this.direction = direction;
        this.quantite = quantite;
        this.prixDeclaration = prixDeclaration;
        this.statut = OrderStatus.PENDING;
        this.datePlacement = LocalDateTime.now();
    }
}
