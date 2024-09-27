package com.example.devcoursed.domain.orders.orders.service;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.orders.exception.OrderException;
import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;

import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orders.repository.OrderRepository;
import com.example.devcoursed.domain.product.product.entity.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Log4j2
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Orders createOrder(OrderDTO orderDTO, Member member) {
        memberRepository.save(member);
        Orders orders = orderDTO.toEntity(member, productRepository); // DTO에서 엔티티로 변환
        return orderRepository.save(orders);      // 엔티티 저장

    }
    public OrderDTO read(Long orderId) {
        Orders orders = orderRepository.findById(orderId).orElseThrow(OrderException.NOT_FOUND::get);
        log.info(orders);
        return new OrderDTO(orders);
    }
    @Transactional
    public void delete(Long orderId) {
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(OrderException.NOT_FOUND::get);
        try {
            orderRepository.delete(orders);
        } catch (Exception e) {
            log.error("예외 발생 코드 : " + e.getMessage());
            throw OrderException.NOT_REMOVED.get();
        }
    }
    public Page<OrderDTO.OrderListDTO> getList(OrderDTO.PageRequestDTO pageRequestDTO) {
        try {
            Sort sort = Sort.by("id").descending();
            Pageable pageable = pageRequestDTO.getPageable(sort);
            Page<Orders> ordersPage = orderRepository.findAll(pageable);
            return ordersPage.map(OrderDTO.OrderListDTO::new);
        } catch (Exception e) {
            throw OrderException.NOT_REMOVED.get();
        }

    }
}