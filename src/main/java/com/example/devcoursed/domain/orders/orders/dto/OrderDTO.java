package com.example.devcoursed.domain.orders.orders.dto;


import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orderItem.dto.OrderItemDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor

//"주문 데이터 전송 객체"
public class OrderDTO {
    private Long id;
    private List<OrderItemDTO> items = new ArrayList<>();
    private Long totalPrice;

    // 기존 엔티티로 변환하는 메서드
    public Orders toEntity(Member member, ProductRepository productRepository) {
        Orders orders = new Orders(0L, member);
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order items cannot be null or empty");
        }
        for (OrderItemDTO itemDTO : items) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            orders.addOrderItem(product, itemDTO.getQuantity(), itemDTO.getPrice());
        }
        return orders;
    }

    // 엔티티로부터 DTO 생성
    public OrderDTO(Orders orders) {
        this.id = orders.getId();
        this.items = orders.getOrderItems().stream()
                .map(orderItem -> new OrderItemDTO(orderItem)) // OrderItemDTO 변환 필요
                .collect(Collectors.toList());
        this.totalPrice = orders.getTotalPrice(); // getTotalPrice 메서드 필요
    }
}
