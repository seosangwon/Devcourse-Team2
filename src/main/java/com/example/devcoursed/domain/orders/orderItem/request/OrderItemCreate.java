package com.example.devcoursed.domain.orders.orderItem.request;

import com.example.devcoursed.domain.orders.orders.entity.Orders;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderItemCreate {

    private Long productId;
    private Long orderId;
    private int quantity;
    private int price;

    @Builder
    public OrderItemCreate(Long productId, Long orderId, int quantity, int price) {
        this.productId = productId;
        this.orderId = orderId;
        this.quantity = quantity;
        this.price = price;
    }


}
