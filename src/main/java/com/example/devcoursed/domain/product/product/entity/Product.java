package com.example.devcoursed.domain.product.product.entity;


import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orderItem.entity.OrderItem;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Long loss;


    @CreatedDate
    private LocalDateTime createdAt;

//    @LastModifiedDate
//    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member maker;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @Builder
    public Product(String name,  Long loss, Member maker) {
        this.name = name;
        this.loss = loss;
    }
    public void setMaker(Member maker) {
        this.maker = maker;
        maker.getProductList().add(this);
    }

    public void changeLoss(Long newLoss){
        this.loss = newLoss;
    }

}