package com.example.devcoursed.domain.orders.orders.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderSummaryDTO {
    private String orderMonth;
    private String productName;
    private long totalQuantity;

    public OrderSummaryDTO(String orderMonth, long totalQuantity) {
        this.orderMonth = orderMonth;
        this.totalQuantity = totalQuantity;
    }
}
