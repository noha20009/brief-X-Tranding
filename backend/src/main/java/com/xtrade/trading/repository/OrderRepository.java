package com.xtrade.trading.repository;

import com.xtrade.trading.model.Order;
import com.xtrade.trading.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTraderIdOrderByDatePlacementDesc(Long traderId);
    List<Order> findByStatut(OrderStatus statut);
    List<Order> findByTraderIdAndStatutOrderByDatePlacementDesc(Long traderId, OrderStatus statut);
}
