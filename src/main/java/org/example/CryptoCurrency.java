package org.example;

public class CryptoCurrency extends Asset <String>{
    private double quantity;

    public CryptoCurrency(int idAsset, String nom, double prixUnitaire, String type, double quantity) {
        super(idAsset, nom, prixUnitaire, type, data);
        this.quantity = quantity;
    }

    @Override
    public double getValue(){
        return prixUnitaire *quantity;
    }
}
