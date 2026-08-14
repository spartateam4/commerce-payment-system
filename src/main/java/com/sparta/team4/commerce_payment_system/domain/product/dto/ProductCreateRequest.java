package com.sparta.team4.commerce_payment_system.domain.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    private String name;

    private String category;

    private int totalPrice;

    private int stock;

    private String description;
}