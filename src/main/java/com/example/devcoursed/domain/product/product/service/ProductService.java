package com.example.devcoursed.domain.product.product.service;

import com.example.devcoursed.domain.member.member.Exception.MemberException;
import com.example.devcoursed.domain.member.member.Exception.MemberTaskException;
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

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository; // 변경 예정

    // 로스율 수정
    public ProductDTO modify(ProductDTO productDTO, long id) {
        log.info("식자재 명: {}", productDTO.getName());
        log.info("입력된 로스 값: {}", productDTO.getLoss());

        // 입력된 로스 값이 null이라면 default loss로 대체
        long loss = (productDTO.getLoss() == null) ? 222L : productDTO.getLoss();

        ProductDTO savedProductDTO = ProductDTO.builder()
                .name(productDTO.getName())
                .loss(loss)
                .build();

        Member member = memberRepository.findById(id).orElseThrow(); // 임시 member 객체 생성. 변경 예정
        Product foundProduct = productRepository.findByMaker(member).orElseThrow(ProductException.PRODUCT_NOT_FOUND::get);

        foundProduct.changeLoss(savedProductDTO.getLoss());
        productRepository.save(foundProduct);
        log.info("DB에 저장된 로스값: {}", foundProduct.getLoss());

        return savedProductDTO;
    }


    // 상품 등록
    public ProductDTO insert(ProductDTO productDTO, Long id){

        // 멤버 조회
        Member member = memberRepository.findById(id)
                .orElseThrow( () -> new NoSuchElementException("Member not found with id :" + id));

        Product product = productDTO.toEntity(member);

        // save
        Product savedProduct = productRepository.save(product);

        // 저장된 데이터로 DTO 반환
        return new ProductDTO(savedProduct);
    }


}
