package com.xtrade.trading.controller;

import com.xtrade.trading.dto.TransactionResponse;
import com.xtrade.trading.service.MarketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST pour l'historique des transactions.
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final MarketService marketService;

    public TransactionController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public List<TransactionResponse> listAll() {
        return marketService.listTransactions();
    }

    @GetMapping("/trader/{traderId}")
    public List<TransactionResponse> listForTrader(@PathVariable Long traderId) {
        return marketService.listTransactionsForTrader(traderId);
    }
}
