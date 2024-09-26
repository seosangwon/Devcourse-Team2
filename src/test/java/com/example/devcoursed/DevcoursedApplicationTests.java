package com.example.devcoursed;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class DevcoursedApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    @DisplayName("식재료 등록 테스트")
    public void testInsertProduct() {
        // Given
        Member member = Member.builder()
                .loginId("membertest")
                .pw("qwer")
                .name("테스트")
                .mImage("아바타")
                .build();

        Member savedMember = memberRepository.save(member);

        // When
        Product product = Product.builder()
                .name("양파")
                .loss(10L)
                .maker(savedMember)
                .build();

//        product.setMaker(savedMember);

        Product savedProduct = productRepository.save(product);

        // Then
        System.out.println("member id: " + savedMember.getId());
        System.out.println("member name: " +  savedMember.getName());
        System.out.println(savedMember.getProductList());

        System.out.println("product's member id: " + savedProduct.getMaker().getId());

        assertNotNull(savedProduct);
        assertEquals(member.getName(), product.getMaker().getName());
        assertEquals("양파", savedProduct.getName());
        assertEquals(10L, savedProduct.getLoss());
    }
}
