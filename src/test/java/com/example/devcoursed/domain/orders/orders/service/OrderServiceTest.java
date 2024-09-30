package com.example.devcoursed.domain.orders.orders.service;

import com.example.devcoursed.DevcoursedApplication;
import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DevcoursedApplication.class)
@Slf4j
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("주문 등록")
    void test(){
        Long memberId = 16L;

        Orders orders = Orders.builder()
               .member(Member.builder()
                       .build())
                .build();

        OrderDTO orderDTO = OrderDTO.builder()
                .orders(orders)
                .build();

        orderService.createOrder(orderDTO,memberId);

    }
}