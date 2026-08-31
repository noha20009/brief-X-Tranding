package com.xtrade.trading.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Ligne du portefeuille : relie un trader à un actif détenu avec sa quantité.
 * (Equivalent fonctionnel de la classe générique Portfolio<T> du brief.)
 */
@Entity
@Table(name = "portfolio_item",
        uniqueConstraints = @UniqueConstraint(name = "uk_trader_asset", columnNames = {"trader_id", "asset_id"}))
@Getter
@Setter
@NoArgsConstructor
public class PortfolioItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trader_id", nullable = false)
    private Trader trader;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private int quantite;

    public PortfolioItem(Trader trader, Asset asset, int quantite) {
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être strictement positive.");
        }
        this.trader = trader;
        this.asset = asset;
        this.quantite = quantite;
    }

    public double getValeur() {
        return asset.getPrixUnitaire() * quantite;
    }
}
