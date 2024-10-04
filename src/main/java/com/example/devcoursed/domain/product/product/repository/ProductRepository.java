package com.example.devcoursed.domain.product.product.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
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
    @Query("SELECT p FROM Product p JOIN FETCH p.maker WHERE p.maker.id = :memberId AND p.createdAt = (SELECT MAX(p2.createdAt) FROM Product p2 WHERE p2.name = p.name ) ORDER BY p.createdAt DESC")
    Page<Product> listAll(Long memberId, Pageable pageable);


    // 단건 조회
    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.maker.id = :memberId")
    Optional<Product> findByName(@Param("name") String name, @Param("memberId") Long memberId);


    // 사용자에게 식재료 name을 받아 평균 로스율 반환
    @Query("SELECT AVG(p.loss) FROM Product p WHERE p.name = :name AND p.loss BETWEEN 0 AND 100")
    Double findAverageLossByName(@Param("name") String name);

    // 최신 로스율 조회
    @Query(value = "SELECT * FROM Product p WHERE p.member_id = :memberId AND p.name = :name ORDER BY p.created_at DESC LIMIT 1", nativeQuery = true)
    Optional<Product> findLatestProductByMakerAndName(@Param("memberId") Long memberId, @Param("name") String name);
}
