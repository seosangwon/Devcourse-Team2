package com.example.devcoursed.domain.product.product.service;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.exception.ProductException;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository; // 변경 예정

    // 식재료 등록
    public ProductDTO insert(ProductDTO productDTO, Long id){
        // 회원 체크 >> 임시 member 객체 생성. 변경 예정
        Member member = memberRepository.findById(id)
                .orElseThrow( () -> new NoSuchElementException("Member not found with id :" + id));

        // 식재료 유무 파악
        productRepository.findByMakerAndName(member, productDTO.getName())
                .ifPresent(product -> {
                    throw ProductException.PRODUCT_ALREADY_EXIST.get();
                });

        // 식재료 등록
        Product savedProduct = productRepository.save(productDTO.toEntity(member));

        return new ProductDTO(savedProduct);
    }

    // 로스율 수정
    public ProductDTO modify(ProductDTO productDTO, long id) {
        // 회원 체크 >> 임시 member 객체 생성. 변경 예정
        Member member = memberRepository.findById(id)
                .orElseThrow( () -> new NoSuchElementException("Member not found with id :" + id));

        // 식재료 유무 파악
        Product foundProduct = productRepository.findByMakerAndName(member, productDTO.getName())
                .orElseThrow(ProductException.PRODUCT_NOT_FOUND::get);

        // 로스율 변경 및 저장
        long loss = (productDTO.getLoss() == null) ? 222L : productDTO.getLoss();
        foundProduct.changeLoss(loss);
        productRepository.save(foundProduct);

        return new ProductDTO(foundProduct); // 변경 가능
    }
}
