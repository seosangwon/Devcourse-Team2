package com.example.devcoursed.domain.product.product.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.product.product.dto.ProductDTO;
import com.example.devcoursed.domain.product.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByMakerAndName(Member maker, String name);


    // 통합 예정 A - 테스트 완료 : name 와 Date를 받아서 전체 사용자의 상품의 평균 로스율 조회 ( 반환값 : Double 형태 avg 1개 )
    @Query("SELECT AVG(p.loss) FROM Product p WHERE p.name = :name AND p.createdAt BETWEEN :startDate AND :endDate AND p.loss BETWEEN 0 AND 100")
    Double findAverageLossByName(@Param("name") String name, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // --------------------------
    // 개인별
    @Query("SELECT new com.example.devcoursed.domain.product.product.dto.ProductDTO$LossRateDTO(p.createdAt,  CAST(p.loss AS double)) " +
            "FROM Product p " +
            "WHERE p.name = :name AND p.maker.id = :memberId " +
            "AND p.createdAt BETWEEN :startDate AND :endDate " +
            "AND p.loss BETWEEN 0 AND 100 " +
            "ORDER BY p.createdAt ASC")
    List<ProductDTO.LossRateDTO> findDailyLossRatePersonal(@Param("memberId") Long memberId,
                                                                @Param("name") String name,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);

    // 전체 데이터 조회
    @Query("SELECT new com.example.devcoursed.domain.product.product.dto.ProductDTO$LossRateDTO(p.createdAt, AVG(p.loss)) " +
            "FROM Product p " +
            "WHERE p.name = :name AND p.createdAt BETWEEN :startDate AND :endDate " +
            "AND p.loss BETWEEN 0 AND 100 " +
            "GROUP BY p.createdAt " +
            "ORDER BY p.createdAt ASC")
    List<ProductDTO.LossRateDTO> findAverageDailyLossRate(@Param("name") String name,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate);

    //---------------------------------


    // B - 테스트 완료 : user와 식재료 이름과 날짜를 입력받아 사용자가 입력했었던 모든 데이터(리스트) 반환 (4-1 로직)
    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.maker.id = :memberId AND p.createdAt BETWEEN :startDate AND :endDate")
    List<Product> findGraphByName(@Param("name") String name, @Param("memberId") Long memberId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate );

    // C - 테스트 완료 : user와 name을 받아서 가장 최신의 로스율을 가진 데이터만 반환 (1번 로직)
    @Query(value = "SELECT * FROM Product p WHERE p.member_id = :memberId AND p.name = :name ORDER BY p.created_at DESC LIMIT 1", nativeQuery = true)
    Optional<Product> findLatestProductByMakerAndName(@Param("memberId") Long memberId, @Param("name") String name);

    // D - 테스트 완료 : user와 name를 받아서 가장 최신의 로스율을 가진 데이터만 반환 (1번 로직)
    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.maker.id = :memberId AND p.createdAt = (SELECT MAX(p2.createdAt) FROM Product p2 WHERE p2.name = p.name)")
    Optional<Product> findByName(@Param("name") String name, @Param("memberId") Long memberId);

    // E - 테스트 완료 : 목록 전체 불러오기 (같은 이름이 있다면 createdAt이 가장 최신인 것들을 골라서 ) (2번 로직)
    @Query("SELECT p FROM Product p JOIN FETCH p.maker WHERE p.maker.id = :memberId AND p.createdAt = (SELECT MAX(p2.createdAt) FROM Product p2 WHERE p2.name = p.name ) ORDER BY p.createdAt DESC")
    Page<Product> listAll(Long memberId, Pageable pageable);




    // 통합예정 F - 테스트 진행중 :  매일매일의 전체 사용자의 평균 로스율을 반환하는 "리스트" ( 반환값 : List 형태 n 개 )
    @Query("SELECT AVG(p.loss) FROM Product p WHERE p.name = :name AND p.createdAt BETWEEN :startDate AND :endDate AND p.loss BETWEEN 0 AND 100")
    Double findLossListByName(@Param("name") String name, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 일치하는 상품명에 대한 전체 평균 로스율 조회
    @Query("SELECT FORMATDATETIME(p.createdAt, 'yyyy-MM-dd') as format, AVG(p.loss) " +
            "FROM Product p " +
            "WHERE p.name = :name AND p.createdAt BETWEEN :startDate AND :endDate AND p.loss BETWEEN 0 AND 100 " +
            "GROUP BY format")
    List<Object[]> findAverageStatisticsByName(@Param("name") String name, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


    // 일치하는 상품명에 대한 개인의 평균 로스율 조회
    @Query(value = "SELECT FORMATDATETIME(p.created_at, 'yyyy-MM-dd') as format, AVG(p.loss) " +
            "FROM Product p " +
            "WHERE p.name = :name AND p.member_id = :memberId AND p.created_at BETWEEN :startDate AND :endDate AND p.loss BETWEEN 0 AND 100 " +
            "GROUP BY format ",
            nativeQuery = true)
    List<Object[]> findAverageStatisticsByMakerAndName(@Param("memberId") Long memberId, @Param("name") String name, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 관리자 통합 목록 전체 조회
    @Query("SELECT p FROM Product p JOIN FETCH p.maker WHERE p.createdAt = (SELECT MAX(p2.createdAt) FROM Product p2 WHERE p2.name = p.name) ORDER BY p.createdAt DESC")
    Page<Product> findAllProducts(Pageable pageable);
}
