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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // 관리자용 목록 전체 불러오기
    public Page<ProductDTO> getProducts(ProductDTO.PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();
        Page<Product> productPage = productRepository.findAllProducts(pageable);
        return productPage.map(ProductDTO::new);
    }

    // ----------- 그래프 처리 -------------
    // 상품의 기간별 평균 로스율
    public List<ProductDTO.AverageResponseDTO> getAverageStatistics(Long memberId, String name, LocalDateTime startDate, LocalDateTime endDate){
        // 기간별 개인의 평균 로스율
        List<Object[]> personalAverages = productRepository.findAverageStatisticsByMakerAndName(memberId, name, startDate, endDate);

        // 기간별 전체 사용자의 평균 로스율
        List<Object[]> allUsersAverages = productRepository.findAverageStatisticsByName(name, startDate, endDate);

        Map<LocalDate, Double> allUserAverages = allUsersAverages.stream()
                .collect(Collectors.toMap(
                        data -> LocalDate.parse((String) data[0]),
                        data -> (Double) data[1]
                ));

        List<LocalDate> dates = new ArrayList<>();
        List<BigDecimal> personalAverageList = new ArrayList<>();
        List<BigDecimal> allUserAverageList = new ArrayList<>();

        // 결과 DTO 리스트 생성
        for (Object[] data : personalAverages) {
            LocalDate date = LocalDate.parse((String) data[0]); // 날짜 파싱
            BigDecimal personalAverage = (BigDecimal) data[1]; // 개인 평균 로스율
            BigDecimal allUserAverage = BigDecimal.valueOf(allUserAverages.getOrDefault(date, null)); // 전체 평균 로스율

            // 리스트에 추가
            dates.add(date);
            personalAverageList.add(personalAverage);
            allUserAverageList.add(allUserAverage);
        }

        // 결과 DTO 생성
        ProductDTO.AverageResponseDTO responseDTO = new ProductDTO.AverageResponseDTO(dates, personalAverageList, allUserAverageList);
        return List.of(responseDTO); // 결과를 리스트 형태로 반환
    }
    // ------------------------------------


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


}
