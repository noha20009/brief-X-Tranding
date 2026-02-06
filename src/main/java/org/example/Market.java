package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.Map;

public class Market {
    private static Market instance;
    private List<Trader> traders;
    private List<Asset> assets;
    private List<Transaction> transactions;

    private Market() {
        this.traders = new ArrayList<>();
        this.assets = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public static Market getInstance() {
        if (instance == null) {
            instance = new Market();
        }
        return instance;
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
        for (Asset a : assets) {
            if (a.getIdAsset() == asset.getIdAsset()) {
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

    public void afficherActifs() {
        for (Asset a : assets) {
            System.out.println(a);
        }
    }

    public void acheterAsset(int idTrader, int idAsset, int quantite) {
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
        transactions.add(new Transaction(trader.getId(),"achet", asset, quantite, asset.getPrixUnitaire())
        );
    }

    public void vendreAsset(int traderId, int idAsset, int quantite) {
        Trader trader = trouverTrader(traderId);
        if (trader == null) {
            throw new IllegalArgumentException("aucun trader trouver");
        }
        Asset asset = trouverAsset(idAsset);
        if (asset == null) {
            throw new IllegalArgumentException("aucun asset trouver");
        }
        if (!trader.getPortfolio().hasAsset(asset, quantite)) {
            throw new IllegalArgumentException("insuffusant quantite dans l'asset");
        }
        double revenue = asset.getPrixUnitaire() * quantite;
        trader.updateBalance(revenue);
        trader.getPortfolio().retirerAsset(asset, quantite);

        transactions.add(new Transaction(trader.getId(), "vendre", asset, quantite, asset.getPrixUnitaire()));
    }

    public void displayPortfolio(int idPerson) {
        Trader trader = trouverTrader(idPerson);
        if (trader == null) {
            System.out.println("trader pas trouver");
            return;
        }
        System.out.println(trader.getPortfolio());
    }

    public void displayTransactions() {
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
    public void displayTransactionForTrader(int traderId){
        Trader trader=trouverTrader(traderId);
        if (trader==null){
            System.out.println("trader avec id " +traderId+ "non trouver");
            return;
        }
        List<Transaction> transactionForTraer = transactions.stream()
                .filter(t->t.getTraderId()==traderId)
                .collect(Collectors.toList());
            if (transactionForTraer.isEmpty()){
                System.out.println("aucun transaction de trader avec id" +trader.getNom());
            }else{
                System.out.println("transaction du trader"+trader.getNom()+ " :");
                transactionForTraer.forEach(System.out::println);
        }
    }




    // Méthode pour trier les transactions par montant (prix * quantite, ascendant, après filtrage éventuel)
    public List<Transaction> sortTransactionsByAmount(String type, String assetName, LocalDateTime startDate, LocalDateTime endDate) {
        return filterTransactions(type, assetName, startDate, endDate).stream()
                .sorted(Comparator.comparing(t -> t.getPrix() * t.getQuantite()))  // Trie par montant ascendant
                .collect(Collectors.toList());
    }

    // Méthode pour afficher les transactions triées par date
    public void displaySortedByDate(String type, String assetName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> sorted = sortTransactionsByDate(type, assetName, startDate, endDate);
        if (sorted.isEmpty()) {
            System.out.println("Aucune transaction trouvée avec les filtres appliqués.");
        } else {
            System.out.println("Transactions triées par date :");
            sorted.forEach(System.out::println);
        }
    }
    public List<Transaction> sortTransactionsByDate(String type, String assetName, LocalDateTime startDate, LocalDateTime endDate) {
        return filterTransactions(type, assetName, startDate, endDate).stream()
                .sorted(Comparator.comparing(Transaction::getDate))  // Trie par date ascendant
                .collect(Collectors.toList());
    }

    // Méthode pour afficher les transactions triées par montant
    public void displaySortedByAmount(String type, String assetName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> sorted = sortTransactionsByAmount(type, assetName, startDate, endDate);
        if (sorted.isEmpty()) {
            System.out.println("Aucune transaction trouvée avec les filtres appliqués.");
        } else {
            System.out.println("Transactions triées par montant :");
            sorted.forEach(System.out::println);
        }

    }

        public List<Transaction> filterTransactions (String type, String assetName, LocalDateTime starDate, LocalDateTime endDate){
            return transactions.stream()
                    .filter(t -> type == null || t.getType().equals(type))
                    .filter(t -> assetName == null || t.getAsset().getNom().equals(assetName))
                    .filter(t -> starDate == null || t.getDate().isAfter(starDate) || t.getDate().isEqual(starDate))
                    .filter(t -> endDate == null || t.getDate().isBefore(endDate) || t.getDate().isEqual(endDate))
                    .collect(Collectors.toList());
        }
    public void displayFilteredTransactions(String type, String assetName, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> filtered = filterTransactions(type, assetName, startDate, endDate);
        if (filtered.isEmpty()) {
            System.out.println("Aucune transaction trouvée avec les filtres appliqués.");
        } else {
            System.out.println("Transactions filtrées :");
            filtered.forEach(System.out::println);
        }

    }


    public double calculateTotalBuyAmount() {
        return transactions.stream()
                .filter(t -> "achet".equals(t.getType()))
                .mapToDouble(t -> t.getPrix() * t.getQuantite())
                .sum();
    }


    public double calculateTotalSellAmount() {
        return transactions.stream()
                .filter(t -> "vendre".equals(t.getType()))
                .mapToDouble(t -> t.getPrix() * t.getQuantite())
                .sum();
    }

    public void displayTotalAmounts() {
        double buyAmount = calculateTotalBuyAmount();
        double sellAmount = calculateTotalSellAmount();
        System.out.println("Montant total des achats (BUY) : " + buyAmount);
        System.out.println("Montant total des ventes (SELL) : " + sellAmount);
    }

    public Map<String, Integer> calculateVolumeByAsset() {
        return transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getAsset().getNom(), Collectors.summingInt(Transaction::getQuantite)));
    }


public void displayVolumeByAsset() {
    Map<String, Integer> volumeByAsset = calculateVolumeByAsset();
    if (volumeByAsset.isEmpty()) {
        System.out.println("Aucun volume échangé trouvé.");
    } else {
        System.out.println("Volume total échangé par actif :");
        volumeByAsset.forEach((assetName, volume) -> System.out.println(assetName + ": " + volume));
    }
}
}

