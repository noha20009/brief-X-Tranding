package com.xtrade.trading.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Classe abstraite représentant un actif financier générique (stock ou crypto).
 * Utilise une stratégie SINGLE_TABLE avec un discriminant pour distinguer les types.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "asset_type", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Stock.class, name = "STOCK"),
        @JsonSubTypes.Type(value = CryptoCurrency.class, name = "CRYPTO")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false)
    private double prixUnitaire;

    @Column(name = "asset_type", insertable = false, updatable = false)
    private String assetType;

    public Asset(String code, String nom, double prixUnitaire) {
        if (prixUnitaire <= 0) {
            throw new IllegalArgumentException("Le prix unitaire doit être strictement positif.");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Le code de l'actif ne peut pas être vide.");
        }
        this.code = code;
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
    }

    @JsonIgnore
    public abstract String getDiscriminator();
}
