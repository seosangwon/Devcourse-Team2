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

@RestController
@RequestMapping("api/v1/products")
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

    // 로스율 수정
    @PutMapping("/")
    public ResponseEntity<ProductDTO> modifyLoss(@AuthenticationPrincipal SecurityUser user,
                                                 @Validated @RequestBody ProductDTO productDTO) {
        long id = user.getId();
        return ResponseEntity.ok(productService.modify(productDTO, id));
    }

    // 평균 로스율 조회
    @GetMapping("/loss/{name}")
    public ResponseEntity<Double> readLoss(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.getAverageLossByName(name));
    }

    // 상품 단건 조회
    @GetMapping("/{name}")
    public ResponseEntity<ProductDTO> read(@AuthenticationPrincipal SecurityUser user, @PathVariable("name") String name){
        Long memberId = user.getId();
        return ResponseEntity.ok(productService.read(name, memberId));
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getList(@AuthenticationPrincipal SecurityUser user, ProductDTO.PageRequestDTO pageRequestDTO) {
        Long memberId = user.getId();
        Page<ProductDTO> productDTOPage = productService.getList(pageRequestDTO, memberId);
        return ResponseEntity.ok(productDTOPage);
    }

}
