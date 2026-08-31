package com.xtrade.trading.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Hérite de Asset et représente une action (stock).
 */
@Entity
@DiscriminatorValue("STOCK")
@Getter
@Setter
@NoArgsConstructor
public class Stock extends Asset {

    public Stock(String code, String nom, double prixUnitaire) {
        super(code, nom, prixUnitaire);
    }

    @Override
    public String getDiscriminator() {
        return "STOCK";
    }
}
