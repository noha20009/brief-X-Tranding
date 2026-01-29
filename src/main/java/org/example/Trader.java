package org.example;

public class Trader {
    private double solde;
    private String portfolio;
    private String histTransaction;

    public Trader(double solde, String portfolio, String histTransaction) {
        this.solde = solde;
        this.portfolio = portfolio;
        this.histTransaction = histTransaction;
    }

    public double getSolde() {
        return solde;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public String getHistTransaction() {
        return histTransaction;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public void setPortfolio(String portfolio) {
        this.portfolio = portfolio;
    }

    public void setHistTransaction(String histTransaction) {
        this.histTransaction = histTransaction;
    }

}
