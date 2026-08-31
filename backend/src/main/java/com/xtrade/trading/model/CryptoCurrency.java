package com.xtrade.trading.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Hérite de Asset et représente une crypto-monnaie.
 */
@Entity
@DiscriminatorValue("CRYPTO")
@Getter
@Setter
@NoArgsConstructor
public class CryptoCurrency extends Asset {

    public CryptoCurrency(String code, String nom, double prixUnitaire) {
        super(code, nom, prixUnitaire);
    }

    @Override
    public String getDiscriminator() {
        return "CRYPTO";
    }
}
