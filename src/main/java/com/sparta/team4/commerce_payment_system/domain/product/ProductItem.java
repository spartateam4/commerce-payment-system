package com.sparta.team4.commerce_payment_system.domain.product;

import lombok.Getter;

@Getter
public class ProductItem {
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String category;

    public ProductItem(Long id, String name, int price, int stockQuantity, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
    }
}
