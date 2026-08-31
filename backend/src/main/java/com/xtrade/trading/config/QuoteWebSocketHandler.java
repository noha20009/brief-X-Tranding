package com.xtrade.trading.config;

import com.xtrade.trading.service.RealtimeQuoteService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Gère les connexions WebSocket des clients pour recevoir les cotations temps réel.
 * À la connexion, le client reçoit immédiatement un instantané des cours, puis
 * des mises à jour périodiques.
 */
@Component
public class QuoteWebSocketHandler extends TextWebSocketHandler {

    private final RealtimeQuoteService quoteService;

    public QuoteWebSocketHandler(RealtimeQuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        quoteService.register(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Les clients n'envoient rien ; on peut ignorer les messages entrants.
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        quoteService.unregister(session);
    }
}
