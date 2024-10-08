package com.example.devcoursed.domain.product.repository;


import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private MemberRepository memberRepository;
    @AfterEach
    void clean(){
        productRepository.deleteAll();
        memberRepository.deleteAll();
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

    //Read Average Loss Test
    @Test
    @Transactional
    public void averageLossTest(){

        Member member = Member.builder()
                .loginId("membertest")
                .pw("qwer")
                .name("테스트")
                .mImage("아바타")
                .build();

        memberRepository.save(member);

        // Given: 테스트 데이터 준비
        Product product1 = Product.builder()
                .name("sub")
                .loss(8L)
                .build();
        product1.setMaker(member);

        Product product2 = Product.builder()
                .name("sub")
                .loss(10L)
                .build();
        product2.setMaker(member);

        Product product3 = Product.builder()
                .name("sub")
                .loss(15L)
                .build();
        product3.setMaker(member);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        Double averageLoss = productRepository.findAverageLossByName("sub",
                                                                    LocalDateTime.of(2024, 10, 3, 0, 0),
                                                                    LocalDateTime.of(2024, 10, 5, 0 ,0));

        if (averageLoss == null) {
            averageLoss = 0.0;
        }

        // 평균 로스율이 예상과 동일한지 검증
        assertEquals(11L, averageLoss, 0.01);

    }

    // Read Test
    @Test
    @Transactional
    public void readTest(){

        // Given: 테스트 데이터 준비
        Member member = Member.builder()
                .loginId("membertest")
                .pw("qwer")
                .name("테스트")
                .mImage("아바타")
                .build();

        memberRepository.save(member);

        Product product1 = Product.builder()
                .name("apple")
                .loss(5L)
                .build();
        product1.setMaker(member);
        Product product2 = Product.builder()
                .name("orange")
                .loss(10L)
                .build();
        product2.setMaker(member);

        productRepository.save(product1);
        productRepository.save(product2);

        String productIfind = "apple";

        Optional<Product> foundProduct = productRepository.findByName(productIfind, member.getId());

        // 검증
        assertTrue(foundProduct.isPresent(), "Product should be present");

    }

    // List Test
    @Test
    @Transactional
    public void listTest(){
        // Given: 테스트 데이터 준비
        Member member = Member.builder()
                .loginId("membertest")
                .pw("qwer")
                .name("테스트")
                .mImage("아바타")
                .build();

        memberRepository.save(member);

        Product product1 = Product.builder()
                .name("water")
                .loss(5L)
                .build();
        product1.setMaker(member);

        Product product2 = Product.builder()
                .name("ice")
                .loss(10L)
                .build();
        product2.setMaker(member);

        Product product3 = Product.builder()
                .name("juice")
                .loss(15L)
                .build();
        product3.setMaker(member);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);

        // 페이지 구성 설정
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<Product> productList = productRepository.listAll(member.getId(), pageable);

        // 검증 과정
        assertNotNull( productList );
        assertEquals(3, productList.getTotalElements());
        assertEquals(1, productList.getTotalPages());
        assertEquals(0, productList.getNumber());
        assertEquals(5, productList.getSize());
        assertEquals(3, productList.getContent().size());

        productList.getContent().forEach(System.out::println);}

}
