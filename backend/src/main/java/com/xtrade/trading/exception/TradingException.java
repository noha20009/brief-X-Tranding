package com.xtrade.trading.exception;

/**
 * Exception métier levée lorsqu'une règle de trading n'est pas respectée
 * (solde insuffisant, quantité invalide, ressource introuvable, etc.).
 */
public class TradingException extends RuntimeException {
    public TradingException(String message) {
        super(message);
    }
}
