package com.example.devcoursed.domain.orders.orderItem.response;

import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private Long productId;  // 상품 ID
    private int quantity;    // 수량
    private int price;       // 가격

    public OrderItemResponse(OrderItem orderItem) {
        this.productId = orderItem.getProduct().getId();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }
}