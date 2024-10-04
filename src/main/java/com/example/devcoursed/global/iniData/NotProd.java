package com.example.devcoursed.global.iniData;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Configuration
@RequiredArgsConstructor
@Profile("!prod")
public class NotProd {

    @Autowired
    @Lazy
    private NotProd self;

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;


    @Bean
    ApplicationRunner initNotProd() {
        return args -> {
            work1();
        };
    }

    @Transactional
    public void work1() {

        if (memberService.count() > 0)
            return;


        //운영자 생성
        MemberDTO.CreateRequestDto requestDto = new MemberDTO.CreateRequestDto();
        requestDto.setLoginId("admin");
        requestDto.setPw("1234");
        requestDto.setEmail("admin@naver.com");
        requestDto.setName("운영자");
        memberService.create(requestDto);

        MemberDTO.Create requestDto2 = new MemberDTO.Create();
        requestDto.setLoginId("test123");
        requestDto.setPw("1234");
        requestDto.setEmail("zmdk1205@naver.com");
        requestDto.setName("서상원");
        memberService.create(requestDto);

        //member 50명 생성
        for (int i = 1; i < 51; i++) {
            MemberDTO.CreateRequestDto createDto = new MemberDTO.CreateRequestDto();
            createDto.setLoginId("abc" + i);
            createDto.setPw("1234");
            createDto.setEmail("abc"+i+"@naver.com");
            createDto.setName("회원" + i);

            memberService.create(createDto);
        }

        // 양파-5, 당근-2, 샐러리-10 생성
        Random random = new Random();

        for (long i = 2; i < 52; i++) {
            Member member = memberRepository.findById(i).get();

            // 양파 생성
            Product onionProduct = Product.builder()
                    .name("양파" + i)
                    .maker(member)
                    .loss(randomLossRate(random, 3, 7)) // 3~7 사이 랜덤값
                    .build();
            productRepository.save(onionProduct);

            // 당근 생성
            Product carrotProduct = Product.builder()
                    .name("당근" + i)
                    .maker(member)
                    .loss(randomLossRate(random, 0, 2)) // 0~2 사이 랜덤 값
                    .build();
            productRepository.save(carrotProduct);

            // 샐러리 생성
            Product celeryProduct = Product.builder()
                    .name("샐러리" + i)
                    .maker(member)
                    .loss(randomLossRate(random, 7, 13)) // 7~13 사이 랜덤값
                    .build();
            productRepository.save(celeryProduct);
        }
    }

    private long randomLossRate(Random random, double min, double max) {
        return  (long)min + (long)(random.nextDouble() * (max - min));
    }


}



