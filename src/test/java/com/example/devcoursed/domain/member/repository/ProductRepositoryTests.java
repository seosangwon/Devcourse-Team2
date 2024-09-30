package com.example.devcoursed.repository;


import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

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


    // Update Test
    @Test
    @Transactional
    @Commit
    public void testUpdate(){

        // 멤버 테이블 임시 저장
        Member member = Member.builder()
                .name("name")
                .loginId("login")
                .pw("pw")
                .mImage("image")
                .build();

        memberRepository.save(member);

        // 상품 임시 저장
        Product product = Product.builder()
                .name("sub")
                .loss(3L)
                .build();
        product.setMaker(member);
        productRepository.save(product);

        Long productId = product.getId();

        Optional<Product> foundProduct = productRepository.findById(productId);

        Assertions.assertTrue(foundProduct.isPresent(), "Product should be present");

        Product productName = foundProduct.get();

        Long newLoss = 5L;
        productName.changeLoss(newLoss);
        productRepository.save(productName);

        // 업데이트 확인
        Optional<Product> updateProduct = productRepository.findById(productId);
        Assertions.assertEquals(newLoss, updateProduct.get().getLoss(), "Product loss should be updated");
    }

}
