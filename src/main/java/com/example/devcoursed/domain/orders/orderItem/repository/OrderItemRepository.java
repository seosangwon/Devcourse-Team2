package com.example.devcoursed.domain.orders.orderItem.repository;

import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
