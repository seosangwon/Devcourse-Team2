package com.example.devcoursed.service;

import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import com.example.devcoursed.domain.product.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class productServiceTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ProductService productService;

    // Insert Test
    @Test
    @Transactional
    @DisplayName("제품 등록 서비스")
    public void testInsertProduct(){



    }
}
