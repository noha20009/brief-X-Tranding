package com.xtrade.trading.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Hérite de Person et représente un utilisateur du système de trading.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Trader extends Person {

    @Column(nullable = false)
    private double balance;

    @Column(unique = true, length = 150)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(length = 30)
    private String role;

    @OneToMany(mappedBy = "trader", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PortfolioItem> portfolio = new ArrayList<>();

    @OneToMany(mappedBy = "trader", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    public Trader(String nom, double balance) {
        super(nom);
        this.balance = balance;
    }

    public Trader(String nom, double balance, String email, String password, String role) {
        super(nom);
        this.balance = balance;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void updateBalance(double montant) {
        this.balance += montant;
    }
}
