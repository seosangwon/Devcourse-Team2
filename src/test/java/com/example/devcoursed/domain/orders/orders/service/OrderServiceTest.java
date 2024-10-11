package com.example.devcoursed.domain.orders.orders.service;

import com.example.devcoursed.DevcoursedApplication;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.exception.MemberException;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.orders.orderItem.dto.OrderItemDTO;
import com.example.devcoursed.domain.orders.orders.dto.OrderDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = DevcoursedApplication.class)
@Slf4j
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("주문 등록")
    void test() {
        Long memberId = 16L; // 테스트에 사용할 회원 ID

        // 회원 정보 조회
        Member member = memberRepository.findById(memberId)
                        .orElseThrow(MemberException.MEMBER_NOT_FOUND::getMemberTaskException);
        assertNotNull(member, "회원이 존재해야 합니다.");

        // 주문 항목 준비
        OrderItemDTO item = new OrderItemDTO();
        item.setProductId(1L); // 예시 제품 ID
        item.setQuantity(2); // 예시 수량
        item.setPrice(5000); // 예시 가격

        // 주문 DTO 생성
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setItems(Collections.singletonList(item)); // 주문 항목 추가
        orderDTO.setTotalPrice(10000L); // 총 가격 설정

        // 주문 등록
        Orders createdOrder = orderService.createOrder(orderDTO, memberId);

        // 결과 검증
        assertNotNull(createdOrder.getId(), "주문 ID는 null이 아니어야 합니다.");
        assertEquals(memberId, createdOrder.getMember().getId(), "회원 ID가 일치해야 합니다.");
        assertEquals(orderDTO.getTotalPrice(), createdOrder.getTotalPrice(), "총 가격이 일치해야 합니다.");
        assertEquals(orderDTO.getItems().size(), createdOrder.getOrderItems().size(), "주문 항목 수가 일치해야 합니다.");
    }
}
