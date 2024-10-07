package com.example.devcoursed.domain.member.member.dto;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder
public class MemberDTO {



    @Data
    public static class CreateRequestDto {

        @NotBlank(message = "로그인 ID는 필수 입력 값 입니다")
        private String loginId;
        @NotBlank(message = "비밀번호는 필수 입력 값 입니다")
        private String email;
        private String pw;
        @NotBlank(message = "닉네임은 필수 입력 값 입니다")
        private String name;
        @JsonProperty("mImage")
        private String mImage;

        public Member toEntity() {
            return Member.builder()
                    .loginId(loginId)
                    .pw(pw)
                    .email(email)
                    .name(name)
                    .mImage(mImage)
                    .build();
        }
    }

    @Data
    public static class CreateResponseDto {
        private String message;

        public CreateResponseDto( String message) {

            this.message=message;
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
        @NotBlank(message = "로그인 ID를 입력해주세요")
        private String loginId;
        @NotBlank(message = "비밀번호를 필수 입력 값 입니다")
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
        private long id;
        private String loginId;
        private String email;
        private String pw;
        private String name;
        private String mImage;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        public Response(Member member) {
            this.id = member.getId();
            this.loginId = member.getLoginId();
            this.pw = member.getPw();
            this.name = member.getName();
            this.mImage = member.getMImage();
            this.createdAt = member.getCreatedAt();
            this.modifiedAt = member.getModifiedAt();
            this.email = member.getEmail();
        }


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangeImage{
        private Long id;
        private MultipartFile mImage;
    }

    @Data
    public static class logoutResponseDto {
        private String message;

        public logoutResponseDto(String message) {
            this.message = message;
        }
    }

    @Data
    public static class FindPWRequestDto {

        private String loginId;
        private String email;
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

