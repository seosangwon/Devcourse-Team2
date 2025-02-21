package com.example.devcoursed.domain.orders.orders.entity;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.product.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(exclude = "orderItems") //순환 참조 방지
@EntityListeners(AuditingEntityListener.class)

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;


    @CreatedDate
    @Column(name = "created_at", updatable = false) // 생성일은 업데이트하지 않음
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at") // 수정일
    private LocalDateTime modifiedAt;


    @ManyToOne  // Member와 관계를 설정
    @JoinColumn(name = "member_id")  // 외래키 설정
    private Member member;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Orders(Long totalPrice, Member member) {
        this.totalPrice = totalPrice;
        this.member = member;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getOrdersList().add(this);
    }

    public void addOrderItem(Product product, int quantity, int price) {
        OrderItem orderItem = OrderItem.builder()
                .orders(this)
                .product(product)
                .quantity(quantity)
                .price(price)
                .build();

        this.orderItems.add(orderItem);
        this.totalPrice += (long) price * quantity;
    }


}