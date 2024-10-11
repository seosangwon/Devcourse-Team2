package com.example.devcoursed.domain.product.product.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProductTaskException extends RuntimeException {
    private final HttpStatus status;

    public ProductTaskException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
