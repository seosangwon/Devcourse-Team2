package com.example.devcoursed.domain.product.product.controller;


import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    @PostMapping("/{id}")
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

}
