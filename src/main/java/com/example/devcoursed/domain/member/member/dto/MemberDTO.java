package com.example.devcoursed.domain.member.member.dto;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.orders.orders.entity.Orders;
import com.example.devcoursed.domain.product.product.entity.Product;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
public class MemberDTO {

    @Data
    public static class Create {
        private String loginId;
        private String pw;
        private String name;
        private String mImage;

        public Member toEntity() {
            return Member.builder()
                    .loginId(loginId)
                    .pw(pw)
                    .name(name)
                    .mImage(mImage)
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    public static class Update {
        private Long id;
        private String loginId;
        private String pw;
        private String name;
        private String mImage;
    }

    @Data
    public static class Login {
        private String loginId;
        private String pw;
    }

    @Data
    public static class Delete {
        private String loginId;
        private String pw;
    }





    @Data
    @AllArgsConstructor
    public static class Response {
        private String loginId;
        private String pw;
        private String name;
        private String mImage;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
    }

//    @Data
//    @AllArgsConstructor
//    public static class ProductGet {
//        private Long id;
//        private List<Product> productList = new ArrayList<>();
//    }
//
//    @Data
//    @AllArgsConstructor
//    public static class OrdersGet {
//        private List<Orders> ordersList = new ArrayList<>();
//    }

}

