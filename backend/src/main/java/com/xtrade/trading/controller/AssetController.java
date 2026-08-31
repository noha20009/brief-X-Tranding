package com.xtrade.trading.controller;

import com.xtrade.trading.dto.AssetRequest;
import com.xtrade.trading.dto.AssetResponse;
import com.xtrade.trading.service.MarketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST pour la gestion des actifs financiers (actions et cryptos).
 */
@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final MarketService marketService;

    public AssetController(MarketService marketService) {
        this.marketService = marketService;
    }

    @GetMapping
    public List<AssetResponse> listAssets() {
        return marketService.listAssets();
    }

    @GetMapping("/{id}")
    public AssetResponse getAsset(@PathVariable Long id) {
        return marketService.getAsset(id);
    }

    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(@Valid @RequestBody AssetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(marketService.createAsset(request));
    }

    @PutMapping("/{id}")
    public AssetResponse updatePrice(@PathVariable Long id, @RequestParam double prix) {
        return marketService.updateAssetPrice(id, prix);
    }
}
