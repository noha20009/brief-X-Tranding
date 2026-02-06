package org.example;

public abstract class Asset{
    protected int idAsset;
    protected String Nom;
    protected double prixUnitaire;

    public Asset(int idAsset, String nom, double prixUnitaire) {
        if (prixUnitaire<=0) throw new IllegalArgumentException("le prix insuffusant");
        this.idAsset = idAsset;
        Nom = nom;
        this.prixUnitaire = prixUnitaire;
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

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public abstract String getType();

    @Override
    public String toString(){
        return "idAsset" +idAsset+ ", name" +Nom+ ",prix" +prixUnitaire+ ",type : " +getType();
    }
//    Logique : Abstraite pour forcer l'implémentation de getType(). Validation : prix positif.
@Override
    public boolean equals(Object o){
        if (this==o) return true;
        if (!(o instanceof Asset)) return false;
        Asset asset =(Asset) o;
        return idAsset == asset.idAsset;
}
@Override
    public int hashCode(){
        return Integer.hashCode(idAsset);
}
}
