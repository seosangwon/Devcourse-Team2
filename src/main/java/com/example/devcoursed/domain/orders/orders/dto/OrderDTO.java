package com.example.devcoursed.domain.orders.orders.dto;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orderItem.dto.OrderItemDTO;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// "주문 데이터 전송 객체"
@Data
@NoArgsConstructor
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
    @Builder
    public OrderDTO(Orders orders) {
        this.id = orders.getId();
        this.items = orders.getOrderItems().stream()
                .map(OrderItemDTO::new) // OrderItemDTO 변환 필요
                .collect(Collectors.toList());
        this.totalPrice = orders.getTotalPrice(); // getTotalPrice 메서드 필요
    }

    // 내부 클래스 - 주문 목록을 위한 DTO
    @Data
    public static class OrderListDTO {
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

    // 내부 클래스 - 페이지 요청 DTO
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageRequestDTO {
        @Builder.Default
        private int page = 1;

        @Builder.Default
        private int size = 10;

        public Pageable getPageable(Sort sort) {
            int pageNum = page < 0 ? 1 : page - 1;
            int sizeNum = size <= 10 ? 10 : size;

            return PageRequest.of(pageNum, sizeNum, sort);
        }
    }

}