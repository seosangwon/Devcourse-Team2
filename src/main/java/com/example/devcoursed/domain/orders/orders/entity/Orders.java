package com.example.devcoursed.domain.orders.orders.entity;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = "orderItems") //순환 참조 방지
@EntityListeners(AuditingEntityListener.class)
@Builder

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
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

    public Orders(Long totalPrice, Member member) {
        this.totalPrice = totalPrice;
        setMember(member);
    }

    public void setMember(Member member) {
        this.member = member;
        member.getOrdersList().add(this);

    }










}