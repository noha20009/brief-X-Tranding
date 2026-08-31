package com.xtrade.trading.repository;

import com.xtrade.trading.model.Transaction;
import com.xtrade.trading.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTraderIdOrderByDateAsc(Long traderId);
    List<Transaction> findAllByOrderByDateAsc();

    // Filtres
    List<Transaction> findByTypeAndTraderIdOrderByDateAsc(TransactionType type, Long traderId);

    // Statistiques
    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByAssetId(Long assetId);
    List<Transaction> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
