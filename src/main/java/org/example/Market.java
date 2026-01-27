package org.example;

import java.util.ArrayList;
import java.util.List;

public class Market {
    private List<Trader> listTrider;
    private List<Asset> listAsset;
    private List<Transaction> listTransaction;

    public Market(List<Trader> listTrider, List<Asset> listAsset, List<Transaction> listTransaction) {
        this.listTrider = new ArrayList<>();
        this.listAsset = new ArrayList<>();
        this.listTransaction = new ArrayList<>();
    }
}
