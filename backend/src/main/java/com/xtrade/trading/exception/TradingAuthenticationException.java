package com.xtrade.trading.exception;

/**
 * Levée lors d'un échec d'authentification (mauvais identifiants).
 */
public class TradingAuthenticationException extends RuntimeException {
    public TradingAuthenticationException(String message) {
        super(message);
    }
}