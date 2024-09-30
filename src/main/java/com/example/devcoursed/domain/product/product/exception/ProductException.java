package com.example.devcoursed.domain.product.product.exception;

import org.springframework.http.HttpStatus;

public enum ProductException {
    PRODUCT_NOT_FOUND("해당 식재료를 찾을 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_ALREADY_EXIST("이미 등록된 식재료입니다.", HttpStatus.CONFLICT);

    private final String message;
    private final HttpStatus status;

    ProductException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public ProductTaskException getProductException() {
        return new ProductTaskException(message, status);
    }
}
