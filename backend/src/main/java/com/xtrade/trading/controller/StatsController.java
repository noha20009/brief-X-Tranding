package com.xtrade.trading.controller;

import com.xtrade.trading.dto.PerformanceResponse;
import com.xtrade.trading.dto.StatsResponse;
import com.xtrade.trading.service.MarketService;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints REST pour les statistiques globales et la performance des traders.
 */
@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final MarketService marketService;

    public StatsController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public StatsResponse getStats() {
        return marketService.getStats();
    }

    @GetMapping("/trader/{traderId}")
    public PerformanceResponse getPerformance(@PathVariable Long traderId) {
        return marketService.getPerformance(traderId);
    }
}
