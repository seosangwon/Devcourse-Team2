//package com.example.devcoursed.domain.orders.orders.entity;
//
//import com.example.devcoursed.domain.member.member.entity.Member;
//import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
//import com.example.devcoursed.domain.orders.orderItem.repository.OrderItemRepository;
//import com.example.devcoursed.domain.orders.orders.entity.Orders;
//import com.example.devcoursed.domain.orders.orders.repository.OrderRepository;
//import com.example.devcoursed.domain.orders.orders.service.OrderService;
//import com.example.devcoursed.domain.product.product.entity.Product;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//class OrdersTest {
//@Autowired
//    private OrderService orderService;
//    private Orders orders;
//    private Member member;
//    private Product product;
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//
//    @BeforeEach
//    void setUp() {
//        // 테스트용 회원 및 상품 객체 생성
//        member = new Member();
//        product = Product.builder()
//                .id(1L)
//                .name("Test Product")
//                .build();
//        System.out.println("product: " + product);
//        // Orders 객체 초기화 (총 가격은 0으로 시작)
//        orders = new Orders(0L, member);
//    }
//
//    @Test
//    void testAddOrderItem() {
//        // 주문 항목 추가
//        System.out.println("product: " + product);
//
//        orders.addOrderItem(product, 2, 100);
//        System.out.println("orders:" + orders);
//        // 주문 항목 리스트가 제대로 추가되었는지 확인
//        assertEquals(1, orders.getOrderItems().size(), "Order item list should have 1 item");
//
//        // 첫 번째 항목이 제대로 추가되었는지 확인
//        OrderItem orderItem = orders.getOrderItems().get(0);
//        System.out.println("orderItem = " + orderItem);
//        System.out.println("orderItem.Product = " + orderItem.getProduct());
//
//        assertEquals(product.getId(), orderItem.getProduct().getId());
//        assertEquals(2, orderItem.getQuantity());
//        assertEquals(100, orderItem.getPrice());
//
//        // 총 가격이 제대로 계산되었는지 확인
//        assertEquals(200L, orders.getTotalPrice(), "Total price should be 200");
//    }
//
//    @Test
//    void testCalculateTotalPrice() {
//        // 여러 개의 주문 항목 추가
//        orders.addOrderItem(product, 1, 100);
//        orders.addOrderItem(product, 2, 200);
//
//        // 총 가격 계산
//        orders.calculateTotalPrice();
//
//        // 총 가격이 맞는지 확인 (1*100 + 2*200 = 500)
//        assertEquals(500L, orders.getTotalPrice(), "Total price should be 500");
//    }
//
//}
