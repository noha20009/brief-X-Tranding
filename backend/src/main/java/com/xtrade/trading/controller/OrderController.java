package com.xtrade.trading.controller;

import com.xtrade.trading.dto.OrderRequest;
import com.xtrade.trading.dto.OrderResponse;
import com.xtrade.trading.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST pour la gestion des ordres conditionnels
 * (limite / stop-loss / take-profit).
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> place(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.placeOrder(request));
    }

    @GetMapping("/trader/{traderId}")
    public List<OrderResponse> list(@PathVariable Long traderId) {
        return orderService.listOrders(traderId);
    }

    @GetMapping("/trader/{traderId}/pending")
    public List<OrderResponse> listPending(@PathVariable Long traderId) {
        return orderService.listPendingOrders(traderId);
    }

    @DeleteMapping("/{orderId}")
    public OrderResponse cancel(@PathVariable Long orderId,
                                @RequestParam Long traderId) {
        return orderService.cancelOrder(traderId, orderId);
    }
}
