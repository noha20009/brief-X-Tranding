package com.xtrade.trading.model;

/**
 * Type d'un ordre conditionnel :
 * LIMIT (achat/vente à un prix limite), STOP_LOSS (vente de protection),
 * TAKE_PROFIT (vente de prise de bénéfice).
 */
public enum OrderType {
    LIMIT,
    STOP_LOSS,
    TAKE_PROFIT
}
