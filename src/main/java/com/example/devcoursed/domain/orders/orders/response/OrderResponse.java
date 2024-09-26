package com.example.devcoursed.domain.orders.orders.response;

import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.orders.orderItem.response.OrderItemResponse;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponse {

    private Long orderId; // 주문 ID
    private List<OrderItemResponse> orderItems;

    public OrderResponse(Long orderId, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.orderItems = orderItems.stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toList());
    }
}

