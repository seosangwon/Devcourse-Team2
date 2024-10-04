package com.example.devcoursed.domain.orders.orders.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orders.dto.OrderSummaryDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findByMember(Member member, Pageable pageable);

    @Query(value = "SELECT DATE_FORMAT(o.created_at, '%Y-%m') AS orderMonth, SUM(oi.quantity) AS totalQuantity " +
            "FROM orders o " +
            "JOIN order_item oi ON o.id = oi.order_id " +
            "GROUP BY DATE_FORMAT(o.created_at, '%Y-%m')", nativeQuery = true)
    List<OrderSummaryDTO> findMonthlyOrderSummary();
}

