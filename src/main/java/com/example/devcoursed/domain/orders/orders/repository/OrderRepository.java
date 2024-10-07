package com.example.devcoursed.domain.orders.orders.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
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

    @Query("SELECT FORMATDATETIME(o.createdAt, 'yyyy-MM') AS orderMonth, SUM(o.totalPrice) AS totalPrice "
            + "FROM Orders o "
            + "WHERE o.member = :member " // 특정 회원 필터링
            + "GROUP BY FORMATDATETIME(o.createdAt, 'yyyy-MM') "
            + "ORDER BY FORMATDATETIME(o.createdAt, 'yyyy-MM')")
    List<Object[]> getMonthlyTotalPrice(@Param("member") Member member);
}


//    @Query("SELECT o FROM Orders o WHERE o.member = :member")
//    List<Orders> findAll(@Param("member") Member member);
