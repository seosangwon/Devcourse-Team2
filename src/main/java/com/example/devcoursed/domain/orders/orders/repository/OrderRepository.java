package com.example.devcoursed.domain.orders.orders.repository;

import com.example.devcoursed.domain.orders.orders.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {
}
