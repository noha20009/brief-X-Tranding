package org.example;

public class Stock extends Asset<Double> {
    private double quantity;

    public Stock(int idAsset, String nom, double prixUnitaire, String type, Double data, double quantity) {
        super(idAsset, nom, prixUnitaire, type, data);
        this.quantity = quantity;
    }

    @Override
    public double getValue(){
        return data * quantity;
    }
}


