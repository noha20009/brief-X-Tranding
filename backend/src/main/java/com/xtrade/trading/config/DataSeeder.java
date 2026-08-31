package com.xtrade.trading.config;

import com.xtrade.trading.model.*;
import com.xtrade.trading.repository.AssetRepository;
import com.xtrade.trading.repository.PortfolioItemRepository;
import com.xtrade.trading.repository.TraderRepository;
import com.xtrade.trading.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initialise la base avec des données de démonstration au premier démarrage.
 */
@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seed(TraderRepository traderRepository,
                                  AssetRepository assetRepository,
                                  PortfolioItemRepository portfolioItemRepository,
                                  TransactionRepository transactionRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {
            ensureDemoAccounts(traderRepository, passwordEncoder);

            if (traderRepository.count() > 0) {
                return;
            }

            // ----- Actifs -----
            Asset aapl = assetRepository.save(new Stock("AAPL", "Apple Inc.", 173.5));
            Asset msft = assetRepository.save(new Stock("MSFT", "Microsoft Corp.", 415.2));
            Asset tsla = assetRepository.save(new Stock("TSLA", "Tesla Inc.", 248.9));
            Asset btc = assetRepository.save(new CryptoCurrency("BTC", "Bitcoin", 61200));
            Asset eth = assetRepository.save(new CryptoCurrency("ETH", "Ethereum", 3420));
            Asset sol = assetRepository.save(new CryptoCurrency("SOL", "Solana", 158.7));

            // ----- Traders (comptes de démo : mot de passe "password") -----
            Trader ali = traderRepository.save(new Trader("Ali Benali", 50000));
            Trader sara = traderRepository.save(new Trader("Sara El Amrani", 30000));
            Trader karim = traderRepository.save(new Trader("Karim Haddad", 100000));

            // ----- Transactions + portefeuilles -----
            exec(transactionRepository, portfolioItemRepository, ali, aapl, TransactionType.ACHAT, 20, 168.2);
            exec(transactionRepository, portfolioItemRepository, ali, btc, TransactionType.ACHAT, 1, 58000);
            exec(transactionRepository, portfolioItemRepository, ali, aapl, TransactionType.VENTE, 5, 175.0);

            exec(transactionRepository, portfolioItemRepository, sara, msft, TransactionType.ACHAT, 10, 410.5);
            exec(transactionRepository, portfolioItemRepository, sara, eth, TransactionType.ACHAT, 5, 3300);
            exec(transactionRepository, portfolioItemRepository, sara, sol, TransactionType.ACHAT, 20, 150.2);

            exec(transactionRepository, portfolioItemRepository, karim, tsla, TransactionType.ACHAT, 30, 240.1);
            exec(transactionRepository, portfolioItemRepository, karim, btc, TransactionType.ACHAT, 2, 60000);
            exec(transactionRepository, portfolioItemRepository, karim, eth, TransactionType.VENTE, 1, 3450);

            System.out.println(">>> Données de démonstration initialisées avec succès.");
        };
    }

    /**
     * Garantit la présence des comptes de démonstration (mot de passe "password"),
     * y compris lorsque la base contient déjà des traders créés avant l'authentification.
     */
    private void ensureDemoAccounts(TraderRepository traderRepository, PasswordEncoder passwordEncoder) {
        upsertDemoAccount(traderRepository, passwordEncoder, "Ali Benali", 50000, "ali@xtrade.com");
        upsertDemoAccount(traderRepository, passwordEncoder, "Sara El Amrani", 30000, "sara@xtrade.com");
        upsertDemoAccount(traderRepository, passwordEncoder, "Karim Haddad", 100000, "karim@xtrade.com");
    }

    private void upsertDemoAccount(TraderRepository traderRepository,
                                   PasswordEncoder passwordEncoder,
                                   String nom,
                                   double balance,
                                   String email) {
        if (traderRepository.existsByEmail(email)) {
            return;
        }
        String encoded = passwordEncoder.encode("password");
        Trader existing = traderRepository.findByNom(nom).orElse(null);
        if (existing != null) {
            existing.setEmail(email);
            existing.setPassword(encoded);
            existing.setRole("ROLE_USER");
            traderRepository.save(existing);
        } else {
            traderRepository.save(new Trader(nom, balance, email, encoded, "ROLE_USER"));
        }
    }

    /**
     * Applique une transaction et met à jour le portefeuille du trader.
     */
    private void exec(TransactionRepository txRepo,
                      PortfolioItemRepository portfolioRepo,
                      Trader trader,
                      Asset asset,
                      TransactionType type,
                      int quantite,
                      double prix) {
        double montant = prix * quantite;
        if (type == TransactionType.ACHAT) {
            trader.updateBalance(-montant);
        } else {
            trader.updateBalance(montant);
        }

        PortfolioItem item = portfolioRepo.findByTraderIdAndAssetId(trader.getId(), asset.getId()).orElse(null);
        int delta = type == TransactionType.ACHAT ? quantite : -quantite;
        if (item == null) {
            item = new PortfolioItem(trader, asset, Math.abs(delta));
        } else {
            item.setQuantite(Math.max(0, item.getQuantite() + delta));
        }
        if (item.getQuantite() == 0) {
            portfolioRepo.delete(item);
        } else {
            portfolioRepo.save(item);
        }

        txRepo.save(new Transaction(type, trader, asset, quantite, prix));
    }
}
