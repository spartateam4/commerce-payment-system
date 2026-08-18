package com.sparta.team4.commerce_payment_system.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductSearchRequest {

    private String category;
    private Integer minPrice;
    private Integer maxPrice;
}