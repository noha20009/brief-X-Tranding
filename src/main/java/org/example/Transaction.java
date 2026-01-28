package org.example;

import java.time.LocalDate;
import java.util.List;

public class Transaction {
    private List<Asset> listAsset;
    private int quantité;
    private double prix;
    private LocalDate date;

    public Transaction(List<Asset> listAsset, int quantité, double prix, LocalDate date) {
        this.listAsset = listAsset;
        this.quantité = quantité;
        this.prix = prix;
        this.date = date;
    }
}
