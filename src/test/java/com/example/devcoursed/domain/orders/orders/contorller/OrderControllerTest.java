package com.example.devcoursed.domain.orders.orders.contorller;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.orders.orderItem.request.OrderItemCreate;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orders.repository.OrderRepository;
import com.example.devcoursed.domain.orders.orders.request.OrdersRequest;
import com.example.devcoursed.domain.prodcut.prodcut.entity.Product;
import com.example.devcoursed.domain.prodcut.prodcut.entity.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void clean(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("발주 등록")
    void test() throws Exception{
        List<OrderItemCreate> orderItems = new ArrayList<>();

        orderItems.add(
                OrderItemCreate.builder()
                        .productId(1L)
                        .orderId(1L)
                        .quantity(10)
                        .price(1000)
                        .build()
        );
        Member member = Member.builder().build();
        memberRepository.save(member);

        OrdersRequest ordersRequest = OrdersRequest.builder()
                .member(member)
                .orderItems(orderItems)
                .build();

        String json = objectMapper.writeValueAsString(ordersRequest);

        mvc.perform(post("/orders")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        assertEquals(1L, memberRepository.findAll().size());
    }

    @Test
    @DisplayName("회원 번호가 없는 발주 등록 ")
    void test2() throws Exception{
        List<OrderItemCreate> orderItems = new ArrayList<>();

        orderItems.add(
                OrderItemCreate.builder()
                        .productId(1L)
                        .quantity(10)
                        .price(1000)
                        .build()
        );

        OrdersRequest ordersRequest = OrdersRequest.builder()
                .orderItems(orderItems)
                .build();

        String json = objectMapper.writeValueAsString(ordersRequest);

        mvc.perform(post("/orders")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청 입니다."))
                .andExpect(jsonPath("$.validation.member").value("회원 정보 필수입니다."))

                .andDo(print());

    }

    @Test
    @DisplayName("발주 안 상품 1개 조회 ")
    @Transactional
    void test3() throws Exception{
//        List<OrderItem> orderItems = new ArrayList<>();
//        List<OrderItemCreate> orderItemCreates = new ArrayList<>();
//        orderItemCreates.add(
//                OrderItemCreate.builder()
//                        .productId(1L)
//                        .quantity(10)
//                        .price(1000)
//                        .build()
//        );
//
//        Member member = Member.builder().build();
//        memberRepository.save(member);
//
//        OrdersRequest ordersRequest = OrdersRequest.builder()
//                .member(member)
//                .orderItems(orderItemCreates)
//                .build();
//
//        Product products = Product.builder().build();
//        productRepository.save(products);
//
//        for (OrderItemCreate orderItemCreate : ordersRequest.getOrderItems()) {
//            Product product = productRepository.findById(orderItemCreate.getProductId())
//                    .orElseThrow(RuntimeException::new);
//
//            OrderItem orderItem = OrderItem.builder()
//                    .product(product)
//                    .quantity(orderItemCreate.getQuantity())
//                    .price(orderItemCreate.getPrice())
//                    .build();
//
//            orderItems.add(orderItem);
//        }
//
//        Orders orders = Orders.builder()
//                .member(member)
//                .orderItems(orderItems)
//                .build();
//
//        orderRepository.save(orders);

        List<OrderItemCreate> orderItemCreates = new ArrayList<>();
        Product product =  Product.builder().id(1L).build();

        productRepository.save(product);
        orderItemCreates.add(
                OrderItemCreate.builder()
                        .productId(product.getId())
                        .quantity(10)
                        .price(1000)
                        .build()
        );

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemCreate orderItemCreate : orderItemCreates) {
            Product product1 = productRepository.findById(orderItemCreate.getProductId())
                    .orElseThrow(RuntimeException::new);
            OrderItem orderItem = OrderItem.builder()
                    .product(product1)
                    .quantity(orderItemCreate.getQuantity())
                    .price(orderItemCreate.getPrice())
                    .build();

            items.add(orderItem);
        }

        Member member = Member.builder().build();
        memberRepository.save(member);

        Orders orders = Orders.builder()
                .member(member)
                .orderItems(items)
                .build();

        orderRepository.save(orders);


        mvc.perform(get("/orders/{id}", orders.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orders.getId())) // 응답에서 orderId 검증
                .andDo(print());

    }

    @Test
    @DisplayName("발주 안의 상품 여러개 조회 ")
    @Transactional
    void test4() throws Exception{


        List<OrderItemCreate> orderItemCreates = new ArrayList<>();
        Product product =  Product.builder().id(1L).build();

        productRepository.save(product);

        Product product1 =  Product.builder().id(2L).build();

        productRepository.save(product1);
        orderItemCreates.add(
                OrderItemCreate.builder()
                        .productId(product.getId())
                        .quantity(10)
                        .price(1000)
                        .build()
        );

        orderItemCreates.add(
                OrderItemCreate.builder()
                        .productId(product1.getId())
                        .quantity(10)
                        .price(1000)
                        .build()
        );
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemCreate orderItemCreate : orderItemCreates) {
            Product product2 = productRepository.findById(orderItemCreate.getProductId())
                    .orElseThrow(RuntimeException::new);
            OrderItem orderItem = OrderItem.builder()
                    .product(product2)
                    .quantity(orderItemCreate.getQuantity())
                    .price(orderItemCreate.getPrice())
                    .build();

            items.add(orderItem);
        }

        Member member = Member.builder().build();
        memberRepository.save(member);

        Orders orders = Orders.builder()
                .member(member)
                .orderItems(items)
                .build();

        orderRepository.save(orders);


        mvc.perform(get("/orders/{id}", orders.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orders.getId())) // 응답에서 orderId 검증
                .andDo(print());

    }

    @Test
    @DisplayName("발주 삭제")
    void test5() throws Exception{

        List<OrderItemCreate> orderItemCreates = new ArrayList<>();
        Product product =  Product.builder().id(1L).build();

        productRepository.save(product);
        orderItemCreates.add(
                OrderItemCreate.builder()
                        .productId(product.getId())
                        .quantity(10)
                        .price(1000)
                        .build()
        );

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemCreate orderItemCreate : orderItemCreates) {
            Product product1 = productRepository.findById(orderItemCreate.getProductId())
                    .orElseThrow(RuntimeException::new);
            OrderItem orderItem = OrderItem.builder()
                    .product(product1)
                    .quantity(orderItemCreate.getQuantity())
                    .price(orderItemCreate.getPrice())
                    .build();

            items.add(orderItem);
        }

        Member member = Member.builder().build();
        memberRepository.save(member);

        Orders orders = Orders.builder()
                .member(member)
                .orderItems(items)
                .build();

        orderRepository.save(orders);

        assertEquals(1L, orders.getId());

        mvc.perform(delete("/orders/{id}", orders.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("존재 하지 않는 발주 삭제")
    void test6() throws Exception{

        List<OrderItemCreate> orderItemCreates = new ArrayList<>();
        Product product =  Product.builder().id(1L).build();

        productRepository.save(product);
        orderItemCreates.add(
                OrderItemCreate.builder()
                        .productId(product.getId())
                        .quantity(10)
                        .price(1000)
                        .build()
        );

        List<OrderItem> items = new ArrayList<>();
        for (OrderItemCreate orderItemCreate : orderItemCreates) {
            Product product1 = productRepository.findById(orderItemCreate.getProductId())
                    .orElseThrow(RuntimeException::new);
            OrderItem orderItem = OrderItem.builder()
                    .product(product1)
                    .quantity(orderItemCreate.getQuantity())
                    .price(orderItemCreate.getPrice())
                    .build();

            items.add(orderItem);
        }

        Member member = Member.builder().build();
        memberRepository.save(member);

        Orders orders = Orders.builder()
                .member(member)
                .orderItems(items)
                .build();

        orderRepository.save(orders);

        assertEquals(1L, orders.getId());

        mvc.perform(delete("/orders/{id}", orders.getId() + 1)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }
}