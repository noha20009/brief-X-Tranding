package com.xtrade.trading.controller;

import com.xtrade.trading.dto.TradeRequest;
import com.xtrade.trading.dto.TransactionResponse;
import com.xtrade.trading.service.MarketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints REST pour les opérations d'achat et de vente d'actifs.
 */
@RestController
@RequestMapping("/api/trades")
public class TradingController {

    private final MarketService marketService;

    public TradingController(MarketService marketService) {
        this.marketService = marketService;
    }

    @PostMapping("/buy")
    public ResponseEntity<TransactionResponse> buy(@Valid @RequestBody TradeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marketService.buyAsset(request));
    }

    @PostMapping("/sell")
    public ResponseEntity<TransactionResponse> sell(@Valid @RequestBody TradeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marketService.sellAsset(request));
    }
}
