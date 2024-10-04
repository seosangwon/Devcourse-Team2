package com.example.devcoursed.domain.member.member.controller;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/adm")
public class AdminController {
    private final MemberService memberService;
    private final ProductService productService;

    //모든 회원 정보 조회하기
    @GetMapping("/members/all")
    public ResponseEntity<Page<MemberDTO.Response>> getMembers(@RequestParam(defaultValue = "0") int page) {
        int pageSize=10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<MemberDTO.Response> responseDto = memberService.readAll(pageable);

        return ResponseEntity.ok(responseDto);
    }

    //모든 회원 식재료 정보 조회하기
    @GetMapping("/products/all")
    public ResponseEntity<Page<ProductDTO>> getProducts(ProductDTO.PageRequestDTO pageRequestDTO) {
        Page<ProductDTO> responseDto = productService.getProducts(pageRequestDTO);
        return ResponseEntity.ok(responseDto);
    }
}
