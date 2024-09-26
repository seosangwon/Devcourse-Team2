package com.example.devcoursed.domain.product.product.service;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    public final ProductRepository productRepository;
    public final MemberRepository memberRepository;

    // 상품 등록
    public ProductDTO insert(ProductDTO productDTO){

        // 멤버 조회
        Member member = memberRepository.findById(productDTO.getMemberId())
                .orElseThrow();

        // Product Entity 만들기
        Product product = Product.builder()
                .name(productDTO.getName())
                .loss(productDTO.getLoss())
                .maker(member)
                .build();

        // save
        Product savedProduct = productRepository.save(product);

        // 저장된 데이터로 DTO 반환
        return new ProductDTO(savedProduct.getId(), savedProduct.getName(), savedProduct.getLoss(), member.getId());
    }


}
