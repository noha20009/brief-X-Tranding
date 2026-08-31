package org.example;

import java.util.ArrayList;
import java.util.List;

public class Trader extends Person {
    private double balance;
    private Portfolio<Asset> portfolio;
//    private List<Transaction> listTransactions = new ArrayList<>();


    public Trader(int id, String nom, double balance, Portfolio<Asset> portfolio) {
        super(id, nom);
        this.balance = balance;
        this.portfolio = new Portfolio<>();
    }
//    public void addTransaction(Transaction t){
//        listTransactions.add(t);
//    }

//    public List<Transaction> getListTransactions() {
//        return listTransactions;
//    }

    public double getBalance() {
        return balance;
    }

    public Portfolio<Asset> getPortfolio() {
        return portfolio;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void updateBalance(double montant){
        this.balance+=montant;
    }
    @Override
    public String toString(){
        return super.toString() + ",Balance :" +balance;
    }


}
