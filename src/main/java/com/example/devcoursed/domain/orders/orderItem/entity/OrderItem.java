package com.example.devcoursed.domain.orders.orderItem.entity;

import com.example.devcoursed.domain.orders.orders.entity.Orders;
import jakarta.persistence.*;
import com.example.devcoursed.domain.product.product.entity.Product;
import lombok.*;

@Entity
@Table(name = "orderItem")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"orders", "product"}) //순환 참조 방지
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    private int quantity;
    private int price;

}