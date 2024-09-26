package com.example.devcoursed.domain.orders.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@Getter
public abstract class Exceptions extends RuntimeException{

    public final Map<String, String> validation = new HashMap<String, String>();

    public Exceptions(String message){
        super(message);
    }

    public abstract int getStatusCode();

    public void addValidation(String field, String message){
        validation.put(field,message);
    }
}
