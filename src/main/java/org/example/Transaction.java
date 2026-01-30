package org.example;

import java.time.LocalDateTime;
import java.util.List;

public class Transaction {
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

    @Override
    public String toString(){
        return type+ "" +asset.getNom()+ "qantite : "
                +quantite + "Prix : " +prix+ "date : " +date;
    }

}
