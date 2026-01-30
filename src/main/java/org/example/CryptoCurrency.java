package org.example;

public class CryptoCurrency extends Asset {

    public CryptoCurrency(int idAsset, String nom, double prixUnitaire) {
        super(idAsset, nom, prixUnitaire);
    }

    @Override
    public String getType() {
        return "Cryptocurrency";

    }

}