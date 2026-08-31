package com.xtrade.trading.repository;

import com.xtrade.trading.model.Trader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TraderRepository extends JpaRepository<Trader, Long> {

    Optional<Trader> findByEmail(String email);

    Optional<Trader> findByNom(String nom);

    boolean existsByEmail(String email);
}