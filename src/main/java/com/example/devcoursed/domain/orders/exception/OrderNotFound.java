package com.example.devcoursed.domain.orders.exception;

public class OrderNotFound extends Exceptions {

    /**
     * status -< 404
     */
    private static final String MESSAGE = "존재하지 않는 글입니다.";
    public OrderNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
