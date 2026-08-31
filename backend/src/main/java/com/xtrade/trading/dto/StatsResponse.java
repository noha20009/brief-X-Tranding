package com.xtrade.trading.dto;

import java.util.Map;

/**
 * Statistiques globales du marché exposées par l'API.
 */
public record StatsResponse(
        long tradersCount,
        long assetsCount,
        int transactionsCount,
        double totalAchats,
        double totalVentes,
        Map<String, Integer> volumeParActif
) {}
