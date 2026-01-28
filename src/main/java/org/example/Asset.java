package org.example;

public abstract class Asset<T> {
    protected int idAsset;
    protected String Nom;
    protected double prixUnitaire;
    protected String type;
    protected T data;

    public Asset(int idAsset, String nom, double prixUnitaire, String type,T data) {
        this.idAsset = idAsset;
        Nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.type = type;
        this.data =data;
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

    public abstract double getValue();
    public T getData(){
        return data;
    }
}
