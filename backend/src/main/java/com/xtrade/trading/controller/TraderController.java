package com.xtrade.trading.controller;

import com.xtrade.trading.dto.PortfolioResponse;
import com.xtrade.trading.dto.TraderRequest;
import com.xtrade.trading.dto.TraderResponse;
import com.xtrade.trading.service.MarketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST pour la gestion des traders et de leurs portefeuilles.
 */
@RestController
@RequestMapping("/api/traders")
public class TraderController {

    private final MarketService marketService;

    public TraderController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public List<TraderResponse> listTraders() {
        return marketService.listTraders();
    }

    @GetMapping("/{id}")
    public TraderResponse getTrader(@PathVariable Long id) {
        return marketService.getTrader(id);
    }

    @PostMapping
    public ResponseEntity<TraderResponse> createTrader(@Valid @RequestBody TraderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marketService.createTrader(request));
    }

    @GetMapping("/{id}/portfolio")
    public PortfolioResponse getPortfolio(@PathVariable Long id) {
        return marketService.getPortfolio(id);
    }
}
