package com.example.devcoursed.domain.product.product.service;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.entity.Product;
import com.example.devcoursed.domain.product.product.exception.ProductException;
import com.example.devcoursed.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberService memberService;

    // 식재료 등록
    public ProductDTO insert(ProductDTO productDTO, Long id) {
        Member member = memberService.getMemberById(id);

        productRepository.findByMakerAndName(member, productDTO.getName())
                .ifPresent(product -> {
                    throw ProductException.PRODUCT_ALREADY_EXIST.getProductException();
                });

        Product savedProduct = productRepository.save(productDTO.toEntity(member));

        return new ProductDTO(savedProduct);
    }

    // 로스율 수정
    public ProductDTO modify(ProductDTO productDTO, long id) {
        Member member = memberService.getMemberById(id);

        Product foundProduct = productRepository.findByMakerAndName(member, productDTO.getName())
                .orElseThrow(ProductException.PRODUCT_NOT_FOUND::getProductException);

        // 로스율 null인 경우 default value로 변경
        long loss = (productDTO.getLoss() == null) ? 222L : productDTO.getLoss();

        Product changeLossProduct = Product.builder()
                                    .name(foundProduct.getName())
                                    .loss(loss)
                                    .maker(member)
                                    .build();

        productRepository.save(changeLossProduct);

        return new ProductDTO(changeLossProduct);
    }


    // 상품의 평균 로스율 조회
    public Double getAverageLossByName(String name, LocalDateTime startDate, LocalDateTime endDate){
        return productRepository.findAverageLossByName(name, startDate, endDate);
    }

    // 상품(식재료) 단건 조회
    public ProductDTO read(String name, Long memberId){
        Product product = productRepository.findByName(name, memberId).orElseThrow(ProductException.PRODUCT_NOT_FOUND::getProductException);;

        return new ProductDTO(product);

    }

    // 상품(식재료) 단건 조회 : 누적된 모든 로스율의 변화를 보여줌
    public ProductDTO readGraph(String name, Long memberId, LocalDateTime startDate, LocalDateTime endDate){
        Product product = productRepository.findGraphByName(name, memberId, startDate, endDate).orElseThrow(ProductException.PRODUCT_NOT_FOUND::getProductException);;

        return new ProductDTO(product);

    }

    // 상품 목록 조회
    public Page<ProductDTO> getList(ProductDTO.PageRequestDTO pageRequestDTO, Long memberId) {
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Product> productPage = productRepository.listAll(memberId, pageable);
        return productPage.map(ProductDTO::new);
    }




    // 로스율 추가 등록
    public ProductDTO addLoss(ProductDTO productDTO, long memberId) {
        Member member = memberService.getMemberById(memberId);

        Product foundProduct = productRepository.findLatestProductByMakerAndName(memberId, productDTO.getName())
                .orElseThrow(ProductException.PRODUCT_NOT_FOUND::getProductException);

        // 로스율 null인 경우 default value로 변경
        long loss = (productDTO.getLoss() == null) ? 222L : productDTO.getLoss();

        Product changeLossProduct = Product.builder()
                .name(foundProduct.getName())
                .loss(loss)
                .maker(member)
                .build();

        productRepository.save(changeLossProduct);

        return new ProductDTO(changeLossProduct);
    }

}
