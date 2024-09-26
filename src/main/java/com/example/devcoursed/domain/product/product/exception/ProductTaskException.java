package com.example.devcoursed.domain.product.product.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ProductTaskException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;
}
