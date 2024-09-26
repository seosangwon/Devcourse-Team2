package com.example.devcoursed.domain.orders.orders.request;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.orders.orderItem.request.OrderItemCreate;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.orders.orders.response.OrderResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class OrdersRequest {

    @NotNull(message = "회원 정보 필수입니다.")
    private Member member;

    private List<OrderItemCreate> orderItems;

    private Long totalPrice;

    @Builder
    public OrdersRequest(Member member, List<OrderItemCreate> orderItems) {
        this.member = member;
        this.orderItems = orderItems;
        this.totalPrice = calculateTotalPrice(); // 객체 생성 시 총액 계산
    }

    private Long calculateTotalPrice() {
        if (orderItems == null || orderItems.isEmpty()) {
            return 0L;
        }
        return orderItems.stream()
                .mapToLong(item -> (long) item.getQuantity() * item.getPrice())
                .sum();
    }

}
