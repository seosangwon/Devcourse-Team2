package com.example.devcoursed.domain.product.product.exception;

import org.springframework.http.HttpStatus;

public enum ProductException {
    PRODUCT_NOT_FOUND("Product Not Found", HttpStatus.INTERNAL_SERVER_ERROR);

    private HttpStatus httpStatus;
    private final ProductTaskException productTaskException;

    ProductException(String message, HttpStatus httpStatus) {
        productTaskException = new ProductTaskException(message, httpStatus);
    }

    public ProductTaskException get() {
        return productTaskException;
    }
}
