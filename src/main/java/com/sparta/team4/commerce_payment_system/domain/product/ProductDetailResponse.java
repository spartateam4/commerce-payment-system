package com.sparta.team4.commerce_payment_system.domain.product;

import lombok.Getter;

@Getter
public class ProductDetailResponse {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String category;
    private String description;
    private String createdAt;
    private String updatedAt;

    public ProductDetailResponse(Long id, String name, int price, int stockQuantity,
                                 String category, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.description = description;
        this.createdAt = "2026-08-12T22:00:00";
        this.updatedAt = "2026-08-12T22:00:00";
    }
}
