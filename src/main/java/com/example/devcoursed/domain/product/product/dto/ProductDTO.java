package com.example.devcoursed.domain.product.product.dto;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.product.product.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    @NotBlank(message = "식재료 이름은 필수 값입니다.")
    private String name;

    @Min(value = 0, message = "로스율은 0 이상이어야 합니다.")
    @Max(value = 100, message = "로스율은 100 이하여야 합니다.")
    private Long loss;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.loss = product.getLoss();
    }



    public Product toEntity(Member member) {
        return Product.builder()
                .name(name)
                .loss(loss)
                .maker(member)
                .build();
    }

    // 내부 클래스 - PageRequestDTO
    @Data
    @AllArgsConstructor
    public static class PageRequestDTO {
        private int page;
        private int size;
        private String sortField;
        private String sortDirection;

        public PageRequestDTO() {
            this.page = 0;
            this.size = 5;
            this.sortField = "id";
            this.sortDirection = "ASC";
        }

        public Pageable getPageable() {
            Sort sort = Sort.by(Sort.Direction.fromString(this.sortDirection), this.sortField);
            return PageRequest.of(this.page, this.size, sort);
        }
    }

    // 내부 클래스 - LossRateDTO
    @Data
    @AllArgsConstructor
    public static class LossRateDTO {
        private LocalDateTime createdAt;
        private Double loss;

    }

    @Data
    @AllArgsConstructor
    public static class LossRateResponseDTO {
        private String title;
        private List<LossRateDTO> data;

    }

}
