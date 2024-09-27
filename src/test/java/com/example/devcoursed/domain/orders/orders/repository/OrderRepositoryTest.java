package com.example.devcoursed.domain.orders.orders.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orders.request.OrdersRequest;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.entity.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class OrderRepositoryTest {

    @Autowired private OrderRepository orderRepository;

    @Autowired private MemberRepository memberRepository;

    @Autowired private ProductRepository productRepository;

    @BeforeEach
    void clean() {
        orderRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("주문 등록")
    @Transactional
    void test(){

        Product product = Product.builder()
                .build();
        productRepository.save(product);

        Member member = Member.builder()
                .build();
        memberRepository.save(member);

        OrdersRequest ordersRequest = OrdersRequest.builder()
                .member(member)
                .build();


        Orders orders = Orders.builder()
                .member(ordersRequest.getMember())
                .build();

        OrderItem item1 = OrderItem.builder()
                .price(5000)
                .quantity(2)
                .orders(orders)
                .product(product)
                .build();
        orders.getOrderItems().add(item1);

        orderRepository.save(orders);

        Orders order = orderRepository.findAll().get(0);
        System.out.println(Arrays.toString(order.getOrderItems().toArray()));
        assertEquals(10000L, order.getTotalPrice());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(1 ,order.getOrderItems().get(0).getProduct().getId());
        assertEquals(1L, order.getMember().getId());
    }

    @Test
    @DisplayName("주문 조회")
    void test2(){

    }

}