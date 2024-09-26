package com.example.devcoursed.domain.orders.orders.entity;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    private Long id;  //주문코드 orders_id, 자바권장인 ordersId로 변경

    private Long totalPrice;

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
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
        OrderItem orderItem = new OrderItem(this, product, quantity, price);
        System.out.println("addOrderItem: " + orderItem);
        this.orderItems.add(orderItem);
        this.totalPrice += price * quantity;  // 총액 계산
        System.out.println("Entity total price: " + totalPrice);
        System.out.println("Entity Order Items: " + this.orderItems.get(0));
    }

    public void calculateTotalPrice() {
        this.totalPrice = this.orderItems.stream()
                .mapToLong(orderItem -> (long) orderItem.getPrice() * orderItem.getQuantity())
                .sum();
    }


    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.changeOrder(null);

    }


}