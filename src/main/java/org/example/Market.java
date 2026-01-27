package org.example;

import java.util.ArrayList;
import java.util.List;

public class Market {
    private List<Trader> listTrader;
    private List<Asset> listAsset;
    private List<Transaction> listTransaction;

    public Market(List<Trader> listTrider, List<Asset> listAsset, List<Transaction> listTransaction) {
        this.listTrader = new ArrayList<>();
        this.listAsset = new ArrayList<>();
        this.listTransaction = new ArrayList<>();
    }

    public void ajouterTrader(Trader trader){

            if (listTrader.contains(trader)){
                System.out.println("ce trader n'existe pas");
            }else {
              listTrader.add(trader);
                System.out.println("trader ajouté avec succés");
            }
    }

    public List<Trader> getListTrader() {
        return listTrader;
    }

    public ajouterAssets(){

    }


}
