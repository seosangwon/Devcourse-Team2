package com.example.devcoursed.domain.orders.orders.dto;

import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orderItem.dto.OrderItemDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderListDTO {
    private Long id;              // 주문 ID
    private Long memberId;        // 회원 ID
    private Long totalPrice;       // 총 가격
    private LocalDateTime createdAt;  // 생성일
    private LocalDateTime modifiedAt; // 수정일
    private List<OrderItemDTO> orderItems; // 주문 항목 리스트

    // 엔티티로부터 DTO 생성
    public OrderListDTO(Orders orders) {
        this.id = orders.getId();
        this.memberId = orders.getMember().getId();
        this.totalPrice = orders.getTotalPrice();
        this.createdAt = orders.getCreatedAt();
        this.modifiedAt = orders.getModifiedAt();
        this.orderItems = orders.getOrderItems().stream()
                .map(OrderItemDTO::new) // OrderItem을 OrderItemDTO로 변환
                .collect(Collectors.toList());
    }

    // 기본 생성자
    public OrderListDTO() {}
}
