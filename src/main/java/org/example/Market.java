package org.example;

import java.util.ArrayList;
import java.util.List;

public class Market {
    private List<Trader> traders;
    private List<Asset> assets;
    private List<Transaction> transactions;

    public Market(List<Trader> listTrider, List<Asset> listAsset, List<Transaction> listTransaction) {
        this.traders = new ArrayList<>();
        this.assets = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public List<Trader> getTraders() {
        return traders;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void displayAssets() {
        for (Asset a : assets) {
            System.out.println(a);
        }
    }


    public void ajouterTrader(Trader trader) {
        traders.add(trader);
    }

    public Trader trouverTrader(int id) {
        for (Trader t : traders) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public void ajouterAsset(Asset asset) {
        for (Asset a :assets){
            if (a.getIdAsset() == asset.getIdAsset()){
                throw new IllegalArgumentException("id de l'asset doit etre unique");
            }
        }
        assets.add(asset);
    }

    public Asset trouverAsset(int idAsset) {
        for (Asset a : assets) {
            if (a.getIdAsset() == idAsset) {
                return a;
            }
        }
        return null;
    }
    public void afficherActifs(){
        for(Asset a:assets){
            System.out.println(a);
        }
    }

    public void acheterAsset(int idTrader,int idAsset,int quantite) {
        Trader trader = trouverTrader(idTrader);
        if (trader == null) {
            throw new IllegalArgumentException("trader pas trouver");
        }
        Asset asset = trouverAsset(idAsset);
            if (asset == null) {
                throw new IllegalArgumentException("asset pas trouver");
            }
            if (quantite <= 0) {
                throw new IllegalArgumentException("quantite insuffusant");
            }
            double cost = asset.getPrixUnitaire() * quantite;
            if (trader.getBalance() < cost) {
                throw new IllegalArgumentException("votre solde insuffisant");
            }
            trader.updateBalance(-cost);
            trader.getPortfolio().ajouterAsset(asset, quantite);
            transactions.add(new Transaction("achet", asset, quantite, asset.getPrixUnitaire())
            );
        }

        public void vendreAsset(int traderId ,int idAsset,int quantite){
            Trader trader =trouverTrader(traderId);
            if (trader==null){
                throw new IllegalArgumentException("aucun trader trouver");
            }
            Asset asset= trouverAsset(idAsset);
            if (asset==null){
                throw new IllegalArgumentException("aucun asset trouver");
            }
            if (!trader.getPortfolio().hasAsset(asset,quantite)){
                throw new IllegalArgumentException("insuffusant quantite dans l'asset");
            }
            double revenue= asset.getPrixUnitaire()*quantite;
            trader.updateBalance(revenue);
            trader.getPortfolio().retirerAsset(asset,quantite);

            transactions.add(new Transaction("vendre",asset,quantite,asset.getPrixUnitaire()));
        }

    public void displayPortfolio(int idPerson){
        Trader trader = trouverTrader(idPerson);
        if(trader == null){
            System.out.println("trader pas trouver");
            return;
        }
        System.out.println(trader.getPortfolio());
    }
    public void displayTransactions(){
        for (Transaction t:transactions){
            System.out.println(t);
        }
    }

}