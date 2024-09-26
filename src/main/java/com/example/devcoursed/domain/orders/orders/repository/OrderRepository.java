package com.example.devcoursed.domain.orders.orders.repository;

import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Orders,Long> {

    Optional<Orders> findById(Long id);
    List<Orders> findAllByOrderByIdDesc();
//    Page<OrderDTO> list(Pageable pageable);
    Page<Orders> findAll(Pageable pageable);
}
