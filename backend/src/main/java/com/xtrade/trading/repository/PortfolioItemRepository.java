package com.xtrade.trading.repository;

import com.xtrade.trading.model.PortfolioItem;
import com.xtrade.trading.model.Trader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioItemRepository extends JpaRepository<PortfolioItem, Long> {
    List<PortfolioItem> findByTraderId(Long traderId);
    Optional<PortfolioItem> findByTraderIdAndAssetId(Long traderId, Long assetId);
}
