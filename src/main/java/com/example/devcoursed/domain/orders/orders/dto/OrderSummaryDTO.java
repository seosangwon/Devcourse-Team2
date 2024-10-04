package com.example.devcoursed.domain.orders.orders.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderSummaryDTO {
    private String orderMonth;
    private int totalQuantity;

    public OrderSummaryDTO(String orderMonth, int totalQuantity) {
        this.orderMonth = orderMonth;
        this.totalQuantity = totalQuantity;
    }

    // getters and setters
}
