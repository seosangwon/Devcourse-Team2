package com.example.devcoursed.domain.member.member.dto;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.prodcut.prodcut.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private Long id;
    private String loginId;
    private String pw;
    private String name;
    private String mImage;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    List<Product> productList = new ArrayList<>();
    List<Orders> ordersList = new ArrayList<>();

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.pw = member.getPw();
        this.name = member.getName();
        this.mImage = member.getMImage();
        this.createdAt = member.getCreatedAt();
        this.modifiedAt = member.getModifiedAt();
//        this.productList = new ArrayList<>();
//        if (member.getProductList() != null) {
//            for (Product product : member.getProductList()) {
//                this.productList.add(new ProductDTO(product));
//            }
//        }
//        this.ordersList = new ArrayList<>();
//        if (member.getOrdersList() != null) {
//            for (Orders orders : member.getOrdersList()) {
//                this.ordersList.add(new OrdersDTO(orders));
//            }
    }
    public Member toEntity() {
        return Member.builder()
                .id(id)
                .loginId(loginId)
                .pw(pw)
                .name(name)
                .mImage(mImage)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .productList(productList)
                .ordersList(ordersList)
                .build();
    }
}
