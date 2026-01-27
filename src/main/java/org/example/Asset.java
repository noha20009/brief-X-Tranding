package org.example;

public class Asset {
    private int idAsset;
    private String Nom;
    private double prixUnitaire;
    private String type;

    public Asset(int idAsset, String nom, double prixUnitaire, String type) {
        this.idAsset = idAsset;
        Nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.type = type;
    }

    public int getIdAsset() {
        return idAsset;
    }

    public String getNom() {
        return Nom;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public String getType() {
        return type;
    }
}
