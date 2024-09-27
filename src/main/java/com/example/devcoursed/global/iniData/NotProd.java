package com.example.devcoursed.global.iniData;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Profile("!prod")
public class NotProd {

    @Autowired
    @Lazy
    private NotProd self;

    private final MemberService memberService;



    @Bean
    ApplicationRunner initNotProd() {
        return args -> {
            work1();
        };
    }

    @Transactional
    public void work1() {

        if(memberService.count() > 0 )
            return ;


        MemberDTO.Create requestDto = new MemberDTO.Create();
        requestDto.setLoginId("admin");
        requestDto.setPw("1234");
        requestDto.setName("운영자");
        // 회원1 DTO 생성
        MemberDTO.Create member1Dto = new MemberDTO.Create();
        member1Dto.setLoginId("abc1234");
        member1Dto.setPw("12345678");
        member1Dto.setName("회원1");

// 회원2 DTO 생성
        MemberDTO.Create member2Dto = new MemberDTO.Create();
        member2Dto.setLoginId("def1234");
        member2Dto.setPw("12345678");
        member2Dto.setName("회원2");

// memberService로 각각의 회원 생성
        memberService.create(requestDto);
        memberService.create(member1Dto);
        memberService.create(member2Dto);



    }


}
