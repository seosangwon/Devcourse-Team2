//package com.example.devcoursed.domain.orders.orders.service;
//
//import com.example.devcoursed.domain.member.member.entity.Member;
//import com.example.devcoursed.domain.orders.orderItem.dto.OrderItemDTO;
//import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;
//import com.example.devcoursed.domain.orders.orders.entity.Orders;
//import com.example.devcoursed.domain.product.product.entity.Product;
//import com.example.devcoursed.domain.product.product.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@Transactional
//class OrderServiceIntegrationTest {
//
//    @Autowired
//    private OrderService orderService;
//
//    @MockBean
//    private ProductRepository productRepository;
//
//    private Member member;
//    private Orders orders;
//    private OrderDTO orderDTO;
//
//    @BeforeEach
//    void setUp() {
//        // 테스트용 Member, Orders, OrderDTO 설정
//        member = new Member;
//        member.setId(1L);
//
//        Product product = new Product();
//        product.setId(1L);
//        product.setName("Test Product");
//
//        orders = new Orders(0L, member);
//        orders.addOrderItem(product, 2, 100);
//
//        orderDTO = new OrderDTO();
//        orderDTO.setItems(List.of(new OrderItemDTO(1L, 2, 100)));
//    }
//
//    @Test
//    void testCreateOrder() {
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
//
//        Orders createdOrder = orderService.createOrder(orderDTO, member);
//
//        // 생성된 주문이 올바르게 반환되는지 검증
//        assertNotNull(createdOrder);
//        assertEquals(member, createdOrder.getMember());
//        assertEquals(1, createdOrder.getOrderItems().size());
//    }
//
//    @Test
//    void testReadOrder() {
//        // 상품 저장을 모킹하여 조회
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
//
//        Orders createdOrder = orderService.createOrder(orderDTO, member);
//        OrderDTO foundOrder = orderService.read(createdOrder.getId());
//
//        // 조회된 주문이 올바르게 반환되는지 검증
//        assertNotNull(foundOrder);
//        assertEquals(1, foundOrder.getItems().size());
//    }
//}
