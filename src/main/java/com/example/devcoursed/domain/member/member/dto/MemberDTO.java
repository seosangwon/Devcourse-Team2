package com.example.devcoursed.domain.member.member.dto;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public class MemberDTO {

    @Data
    public static class Create {
        private String loginId;
        private String pw;
        private String name;
        @JsonProperty("mImage")
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
    public static class LoginRequestDto {
        private String loginId;
        private String pw;
    }

    @Data
    public static class LoginResponseDto {
        private long id;
        private String LoginId;
        private String name;
        private String mImage;
        private String accessToken;
        private String refreshToken;

        public LoginResponseDto(Member member) {
            this.id = member.getId();
            this.LoginId = member.getLoginId();
            this.name = member.getName();
            this.mImage = member.getMImage();
        }


    }

    @Data
    public static class RefreshAccessTokenRequestDto {
        private String refreshToken;
    }

    @Data
    public static class RefreshAccessTokenResponseDto {
        private String accessToken;
        private String message;

        public RefreshAccessTokenResponseDto(String accessToken , String message) {
            this.accessToken = accessToken;
            this.message = message;
        }

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

    @Data
    @AllArgsConstructor
    public static class ChangeImage {
        private Long id;
        private String mImage;
    }

    @Data
    public static class logoutResponseDto {
        private String message;

        public logoutResponseDto(String message) {
            this.message = message;
        }
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

