package com.example.devcoursed.domain.product.product.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByMakerAndName(Member maker, String name);

    // 목록 전체 불러오기
    @Query("SELECT p FROM Product p ORDER BY p.id")
    Page<ProductDTO> listAll(Pageable pageable);

    // 이름별 로스율을 구하기 위한 제품이름에 맞는 항목만 반환한는 문장 // 삭제 예정
    @Query("SELECT p FROM Product p WHERE p.name = :name ORDER BY p.id")
    Optional<ProductDTO> findByName(String name);

    // 사용자에게 식재료 name을 받아 평균 로스율 반환
    @Query("SELECT AVG(p.loss) FROM Product p WHERE p.name = :name")
    Double findAverageLossByName(@Param("name") String name);

}
