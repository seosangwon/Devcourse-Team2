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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberService memberService;

    // 식재료 등록 : 테스트 완료
    public ProductDTO insert(ProductDTO productDTO, Long id) {
        Member member = memberService.getMemberById(id);

        productRepository.findByMakerAndName(member, productDTO.getName())
                .ifPresent(product -> {
                    throw ProductException.PRODUCT_ALREADY_EXIST.getProductException();
                });

        Product savedProduct = productRepository.save(productDTO.toEntity(member));

        return new ProductDTO(savedProduct);
    }

    // 로스율 추가 등록 : 테스트 완료
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


    // 통합 예정 : A - 테스트 완료 : name 와 Date를 받아서 전체 사용자의 상품의 평균 로스율 조회
    public Double getAverageLossByName(String name, LocalDateTime startDate, LocalDateTime endDate){
        return productRepository.findAverageLossByName(name, startDate, endDate);
    }

    // B - 테스트 완료 : user와 식재료 이름과 날짜를 입력받아 사용자가 입력했었던 모든 데이터(리스트) 반환 (4-1로직)
    public List<ProductDTO> readGraph(String name, Long memberId, LocalDateTime startDate, LocalDateTime endDate){
//        Product product = productRepository.findGraphByName(name, memberId, startDate, endDate).orElseThrow(ProductException.PRODUCT_NOT_FOUND::getProductException);;
//
//        return new ProductDTO(product);
        // Product 리스트를 조회
        List<Product> products = productRepository.findGraphByName(name, memberId, startDate, endDate);

        // 결과가 없을 때 예외 처리
        if (products.isEmpty()) {
            throw ProductException.PRODUCT_NOT_FOUND.getProductException();
        }

        // Product 리스트를 ProductDTO 리스트로 변환하여 반환
        return products.stream()
                .map(ProductDTO::new)  // 각 Product를 ProductDTO로 변환
                .collect(Collectors.toList());

    }

    // C - 테스트 완료 : user와 name을 받아서 가장 최신에 수정된 데이터만 반환 (1번 로직)
    public ProductDTO findLatestProduct(Long memberId, String name){
        Product product = productRepository.findLatestProductByMakerAndName(memberId, name).orElseThrow(ProductException.PRODUCT_NOT_FOUND::getProductException);

        return new ProductDTO(product);
    }

    // D - 테스트 완료 : user와 name를 받아서 가장 최신의 로스율을 가진 데이터만 반환 (1번 로직)
    public ProductDTO read(String name, Long memberId){
        Product product = productRepository.findByName(name, memberId).orElseThrow(ProductException.PRODUCT_NOT_FOUND::getProductException);;

        return new ProductDTO(product);

    }

    // E - 테스트 완료 : 목록 전체 불러오기 (같은 이름이 있다면 createdAt이 가장 최신인 것들을 골라서 ) (2번 로직)
    public Page<ProductDTO> getList(ProductDTO.PageRequestDTO pageRequestDTO, Long memberId) {
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Product> productPage = productRepository.listAll(memberId, pageable);
        return productPage.map(ProductDTO::new);
    }

    // 상품 목록 전체 조회 (member 로직)
    public Page<ProductDTO> getProducts(ProductDTO.PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductDTO::new);
    }

    //---------------------
    // G - 테스트 완료 : 그래프를 위한 리스트 2개 불러오기 (4번 로직)
    public List<ProductDTO.LossRateResponseDTO> getDailyLossRates(Long memberId, String name, LocalDateTime startDate, LocalDateTime endDate) {
        // 개인의 로스율 데이터
        List<ProductDTO.LossRateDTO> userLossRates = productRepository.findDailyLossRatePersonal(memberId, name, startDate, endDate);

        // 전체 이용자의 로스율 데이터
        List<ProductDTO.LossRateDTO> totalLossRates = productRepository.findAverageDailyLossRate(name, startDate, endDate);

        // 개인 로스율 데이터 셋
        ProductDTO.LossRateResponseDTO userLossRateResponse = new ProductDTO.LossRateResponseDTO("개인 로스율", userLossRates);

        // 전체 이용자 로스율 데이터 셋
        ProductDTO.LossRateResponseDTO totalLossRateResponse = new ProductDTO.LossRateResponseDTO("전체 이용자 로스율", totalLossRates);

        // 두 데이터를 리스트로 반환
        return Arrays.asList(userLossRateResponse, totalLossRateResponse);
    }

    // ----------------------

}
