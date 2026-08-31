package org.example;

public class Stock extends Asset{
    public Stock(int idAsset, String nom, double prixUnitaire) {
        super(idAsset, nom, prixUnitaire);
    }
    @Override
    public String getType(){
        return "stock";
    }
}
//Logique : Simple héritage, type fixe.


