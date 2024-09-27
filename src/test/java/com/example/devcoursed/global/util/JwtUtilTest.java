package com.example.devcoursed.global.util;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;



@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JwtUtilTest {
    @Autowired
    private MemberRepository memberRepository;


    @Test
    @DisplayName("jwt 토큰 생성 ")
    void t1() {
        Map<String, Object> data = Map.of("loginId", "abcd", "id", "1","authorities","ROLE_MEMBER");
        String token = JwtUtil.encode(data);
        System.out.println(token);

        Assertions.assertThat(token).isNotNull();


    }

}