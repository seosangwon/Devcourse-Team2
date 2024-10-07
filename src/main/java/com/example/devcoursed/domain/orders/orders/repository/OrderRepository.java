package com.example.devcoursed.domain.orders.orders.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orders.dto.OrderSummaryDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    Page<Orders> findByMember(Member member, Pageable pageable);

//    @Query("SELECT new com.example.devcoursed.domain.orders.orders.dto.OrderSummaryDTO("
//            + "FORMATDATETIME(oi.orders.createdAt, 'yyyy-MM') || ' - ' || p.name, SUM(oi.price)) "
//            + "FROM OrderItem oi "
//            + "JOIN oi.product p "
//            + "WHERE FORMATDATETIME(oi.orders.createdAt, 'yyyy-MM') = :month " // 특정 월 필터링
//            + "GROUP BY FORMATDATETIME(oi.orders.createdAt, 'yyyy-MM'), p.name, oi.orders.createdAt "
//            + "ORDER BY FORMATDATETIME(oi.orders.createdAt, 'yyyy-MM'), p.name")
//    List<OrderSummaryDTO> getOrdersSummary(@Param("month") String month);

    @Query("SELECT o FROM Orders o WHERE o.member = :member")
    List<Orders> findAll(@Param("member") Member member);
}
