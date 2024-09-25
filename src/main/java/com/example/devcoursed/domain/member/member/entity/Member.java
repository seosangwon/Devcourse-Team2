package com.example.devcoursed.domain.member.member.entity;

import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.prodcut.prodcut.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.query.Order;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true)
    private String loginId;

    private String pw;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String mImage;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy = "maker" , cascade = CascadeType.ALL , orphanRemoval = true)
    @ToString.Exclude
    List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL , orphanRemoval = true)
    @ToString.Exclude
    List<Orders> ordersList = new ArrayList<>();









}
