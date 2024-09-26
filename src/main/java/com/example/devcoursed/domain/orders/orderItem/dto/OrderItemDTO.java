package com.example.devcoursed.domain.orders.orderItem.dto;

import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.product.product.entity.Product;
import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;  // 상품 ID
    private int quantity;  // 수량
    private int price;  // 사용자가 입력한 가격

    // DTO -> 엔티티 변환 메서드
    public OrderItem toEntity(Product product, Orders orders) {
        return OrderItem.builder()
                .product(product)
                .orders(orders)
                .quantity(this.quantity)
                .price(this.price)
                .build();
    }
    // 엔티티로부터 DTO 생성
    public OrderItemDTO(OrderItem orderItem) {
        this.productId = orderItem.getProduct().getId();
        this.quantity = orderItem.getQuantity();
        this.price = orderItem.getPrice();
    }

    // 기본 생성자
    public OrderItemDTO() {}
}
