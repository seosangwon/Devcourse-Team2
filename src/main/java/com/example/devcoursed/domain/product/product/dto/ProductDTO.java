package com.example.devcoursed.domain.product.product.dto;

import com.example.devcoursed.domain.product.product.entity.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "식재료 이름은 필수 값입니다.")
    private String name;

    @Min(value = 0, message = "로스율은 0 이상이어야 합니다.")
    @Max(value = 100, message = "로스율은 100 이하여야 합니다.")
    private Long loss;

    public ProductDTO(Product product) {
        this.name = product.getName();
        this.loss = product.getLoss();
    }

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .loss(loss)
                .build();
    }

}
