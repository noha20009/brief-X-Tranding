package com.xtrade.trading.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Enregistre une opération de trading (achat / vente).
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trader_id", nullable = false)
    private Trader trader;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;

    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false)
    private double prixUnitaire;

    /** Frais de transaction appliqués sur le montant brut (nul pour les anciennes transactions). */
    @Column
    private Double frais;

    @Column(nullable = false)
    private LocalDateTime date;

    public Transaction(TransactionType type, Trader trader, Asset asset, int quantite, double prixUnitaire) {
        this(type, trader, asset, quantite, prixUnitaire, 0.0);
    }

    public Transaction(TransactionType type, Trader trader, Asset asset, int quantite,
                       double prixUnitaire, double frais) {
        this.type = type;
        this.trader = trader;
        this.asset = asset;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.frais = frais;
        this.date = LocalDateTime.now();
    }

    public double getMontantTotal() {
        return prixUnitaire * quantite;
    }
}
