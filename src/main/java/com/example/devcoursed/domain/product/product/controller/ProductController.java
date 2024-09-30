package com.example.devcoursed.domain.product.product.controller;

import com.example.devcoursed.domain.product.product.dto.PageRequestDTO;
import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDTO> register(@PathVariable Long id, @Validated @RequestBody ProductDTO productDTO) {
        ProductDTO registProduct = productService.insert(productDTO, id);
        return ResponseEntity.ok(registProduct);
    }

    // 로스율 수정
    @PutMapping("/{id}") // 임시 path
    public ResponseEntity<ProductDTO> modifyLoss(@PathVariable Long id,
                                             @Validated @RequestBody ProductDTO productDTO) {

        return ResponseEntity.ok(productService.modify(productDTO, id));
    }

    // 평균 로스율 조회
    @GetMapping("/{name}")
    public ResponseEntity<Double> readLoss(@PathVariable("name") String name) {
        Double averageLoss = productService.getAverageLossByName(name);

        return ResponseEntity.ok(averageLoss);
    }

    // 상품 목록 조회
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getList(PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(productService.getList(pageRequestDTO));
    }

}
