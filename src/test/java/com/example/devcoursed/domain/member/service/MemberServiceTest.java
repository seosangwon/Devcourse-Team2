package com.example.devcoursed.domain.member.service;

import com.example.devcoursed.domain.member.member.exception.MemberTaskException;
import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.member.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("비밀번호 인코딩 테스트 ")
    void t1() {
        //given
        MemberDTO.Create createRequestDto = new MemberDTO.Create();
        createRequestDto.setLoginId("loginId1234");
        createRequestDto.setName("홍길동");
        String password = "1234";
        createRequestDto.setPw(password);

        //when
        MemberDTO.Create responseDto = memberService.create(createRequestDto);
        Member member = memberRepository.findByLoginId("loginId1234").get();

        //then
        Assertions.assertThat(password).isNotEqualTo(member.getPw());


    }

    @Test
    @DisplayName("중복 회원가입 테스트")
    void t2() {
        //given
        MemberDTO.Create createRequestDto = new MemberDTO.Create();
        createRequestDto.setLoginId("loginId1234");
        createRequestDto.setName("홍길동");
        String password = "1234";
        createRequestDto.setPw(password);

        MemberDTO.Create newCreateRequestDto = new MemberDTO.Create();
        newCreateRequestDto.setLoginId("loginId1234");
        newCreateRequestDto.setName("홍길순");
        String newPassword = "123456789";
        newCreateRequestDto.setPw(newPassword);


        //when
        memberService.create(createRequestDto);


        //then
        Throwable thrown = Assertions.catchThrowable(() -> {
            memberService.create(newCreateRequestDto);
        });
        Assertions.assertThat(thrown)
                .isInstanceOf(MemberTaskException.class)
                .hasMessageContaining("회원가입에 실패했습니다");


    }

}
