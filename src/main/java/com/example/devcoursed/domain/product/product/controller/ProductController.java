package com.example.devcoursed.domain.product.product.controller;

import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.service.ProductService;
import com.example.devcoursed.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    // 통합 예정 A - 테스트 완료 : name 와 Date를 받아서 전체 사용자의 상품의 평균 로스율 조회
    @GetMapping("/loss/{name}")
    public ResponseEntity<Double> readLoss(@PathVariable("name") String name,
                                           @RequestParam("startDate") String startDate,
                                           @RequestParam("endDate") String endDate) {
        // 프론트에서 받는 데이터 확인
        log.info("로스율 조회하는 상품: {}", name);
        log.info("시작 날짜: {}", startDate);

        // LocalDate를 LocalDateTime으로 변환
        ZonedDateTime startDateTime = ZonedDateTime.parse(startDate); // 시작 날짜의 자정 2024-10-04 00:00:00.000000
        ZonedDateTime endDateTime = ZonedDateTime.parse(endDate).with(LocalTime.MAX); // 종료 날짜의 마지막 순간 (23:59:59)
        log.info("변환된 DateTime: {}", startDateTime);

        return ResponseEntity.ok(productService.getAverageLossByName(name, startDateTime.toLocalDateTime(), endDateTime.toLocalDateTime()));
    }

    // B - 테스트 완료 : 식재료 이름과 날짜를 입력받아 사용자가 입력했었던 모든 데이터(리스트) 반환 (4-1로직)
    @GetMapping("/{name}")
    public ResponseEntity<List<ProductDTO>> readGraph(@AuthenticationPrincipal SecurityUser user,
                                                      @PathVariable("name") String name,
                                                      @RequestParam("startDate") LocalDate startDate,
                                                      @RequestParam("endDate") LocalDate endDate){
        Long memberId = user.getId();

        // LocalDate를 LocalDateTime으로 변환
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 시작 날짜의 자정
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 종료 날짜의 마지막 순간 (23:59:59)

        return ResponseEntity.ok(productService.readGraph(name, memberId, startDateTime, endDateTime));
    }

    // C - 테스트 완료 : user와 name을 받아서 가장 최신에 수정된 데이터만 반환
    @GetMapping("/losslatest/{name}")
    public ResponseEntity<ProductDTO> findLatestProduct(@AuthenticationPrincipal SecurityUser user, @PathVariable("name") String name) {

        Long memberId = user.getId();
        return ResponseEntity.ok(productService.findLatestProduct(memberId, name));
    }

    // D - 테스트 완료 : user와 name를 받아서 가장 최신의 로스율을 가진 데이터만 반환 (1번 로직)
    @GetMapping("/read/{name}")
    public ResponseEntity<ProductDTO> read(@AuthenticationPrincipal SecurityUser user, @PathVariable("name") String name){

        Long memberId = user.getId();
        return ResponseEntity.ok(productService.read(name, memberId));
    }

    //  E - 테스트 완료 : 목록 전체 불러오기 (같은 이름이 있다면 createdAt이 가장 최신인 것들을 골라서 ) (2번 로직)
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getList(@AuthenticationPrincipal SecurityUser user, ProductDTO.PageRequestDTO pageRequestDTO) {
        Long memberId = user.getId();
        Page<ProductDTO> productDTOPage = productService.getList(pageRequestDTO, memberId);
        return ResponseEntity.ok(productDTOPage);
    }

    // G - 테스트 완료 : 그래프를 위한 리스트 2개 불러오기 (4번 로직)
    @GetMapping("/loss-rates/{name}")
    public ResponseEntity<List<ProductDTO.LossRateResponseDTO>> getLossRates(@AuthenticationPrincipal SecurityUser user,
                                                                             @PathVariable("name") String name,
                                                                             @RequestParam("startDate") LocalDate startDate,
                                                                             @RequestParam("endDate") LocalDate endDate) {
        Long userId = user.getId();

        // LocalDate를 LocalDateTime으로 변환
        LocalDateTime startDateTime = startDate.atStartOfDay(); // 시작 날짜의 자정
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // 종료 날짜의 마지막 순간 (23:59:59)

        List<ProductDTO.LossRateResponseDTO> lossRates = productService.getDailyLossRates(userId, name, startDateTime, endDateTime);

        return ResponseEntity.ok(lossRates);
    }

}
