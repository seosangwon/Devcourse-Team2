package com.example.devcoursed.domain.product.product.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByMaker(Member maker);

    // 목록 전체 불러오기
    @Query("SELECT p FROM Product p ORDER BY p.id")
    Page<ProductDTO> listAll(Pageable pageable);

    // 이름별 로스율을 구하기 위한 제품이름에 맞는 항목만 반환한는 문장
    @Query("SELECT p FROM Product p WHERE p.name = :name ORDER BY p.id")
    Page<ProductDTO> findByName(String name, Pageable pageable);

}
