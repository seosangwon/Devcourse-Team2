package com.example.devcoursed.domain.product.product.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequestDTO {

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
