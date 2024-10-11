package com.example.devcoursed.domain.product.product.controller;

import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.service.ProductService;
import com.example.devcoursed.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    // 식재료 등록
    @PostMapping("/")
    public ResponseEntity<ProductDTO> register(@AuthenticationPrincipal SecurityUser user,
                                               @Validated @RequestBody ProductDTO productDTO) {
        long id = user.getId();
        return ResponseEntity.ok(productService.insert(productDTO, id));
    }

    // 로스율 추가 등록 >> 기존의 수정 대체
    @PostMapping("/loss")
    public ResponseEntity<ProductDTO> addLoss(@AuthenticationPrincipal SecurityUser user,
                                              @Validated @RequestBody ProductDTO productDTO) {
        long id = user.getId();
        return ResponseEntity.ok(productService.addLoss(productDTO, id));
    }


    // 통계용 평균 로스율
    @GetMapping("/loss/{name}")
    public ResponseEntity<List<ProductDTO.AverageResponseDTO>> getAverageLossStatistics(@AuthenticationPrincipal SecurityUser user,
                                                                                        @PathVariable String name,
                                                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") OffsetDateTime startDate,
                                                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX") OffsetDateTime endDate) {
        Long memberId = user.getId();

        // 타임존 변경
        ZonedDateTime kstStartDate = startDate.atZoneSameInstant(ZoneId.of("Asia/Seoul"));
        ZonedDateTime kstEndDate = endDate.atZoneSameInstant(ZoneId.of("Asia/Seoul"));

        List<ProductDTO.AverageResponseDTO> statistics = productService.getAverageStatistics(memberId, name, kstStartDate.toLocalDateTime(), kstEndDate.toLocalDateTime());
        return ResponseEntity.ok(statistics);
    }


    // D - 테스트 완료 : user와 name를 받아서 가장 최신의 로스율을 가진 데이터만 반환 (1번 로직)
    @GetMapping("/read/{name}")
    public ResponseEntity<ProductDTO> read(@AuthenticationPrincipal SecurityUser user, @PathVariable("name") String name){

        Long memberId = user.getId();
        return ResponseEntity.ok(productService.read(name, memberId));
    }

    // 사용자용 상품 목록 전체 조회 (같은 이름이 있다면 createdAt이 가장 최신인 것들을 골라서 ) (2번 로직)
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getList(@AuthenticationPrincipal SecurityUser user, ProductDTO.PageRequestDTO pageRequestDTO) {
        Long memberId = user.getId();
        Page<ProductDTO> productDTOPage = productService.getList(pageRequestDTO, memberId);
        return ResponseEntity.ok(productDTOPage);
    }

    // 상품 이름 검색
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(@RequestParam("keyword") String keyword,
                                                           @AuthenticationPrincipal SecurityUser user,
                                                           ProductDTO.PageRequestDTO pageRequestDTO) {

        Long memberId = user.getId();

        // '사과' 키워드로 검색된 결과를 가져옴
        Page<ProductDTO> productDTOPage = productService.searchProducts(keyword, pageRequestDTO, memberId);

        return ResponseEntity.ok(productDTOPage);
    }


}
