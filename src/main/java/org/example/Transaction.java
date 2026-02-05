package org.example;

import java.time.LocalDateTime;
import java.util.List;

public class Transaction {
    private int traderId;
    private String type;
    private Asset asset;
    private int quantite;
    private double prix;
    private LocalDateTime date;

    public Transaction(String type, Asset asset, int quantite, double prix) {
        this.type = type;
        this.asset = asset;
        this.quantite = quantite;
        this.prix = prix;
        this.date = LocalDateTime.now();
    }

    public Transaction(int traderId, String type, Asset asset, double prix, LocalDateTime date, int quantite) {
        this.traderId = traderId;
        this.type = type;
        this.asset = asset;
        this.prix = prix;
        this.date = date;
        this.quantite = quantite;
    }

    public int getTraderId() {
        return traderId;
    }

    public String getType() {
        return type;
    }

    public Asset getAsset() {
        return asset;
    }

    public int getQuantite() {
        return quantite;
    }

    public double getPrix() {
        return prix;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString(){
        return type+ "" +asset.getNom()+ "qantite : "
                +quantite + "Prix : " +prix+ "date : " +date;
    }

}
