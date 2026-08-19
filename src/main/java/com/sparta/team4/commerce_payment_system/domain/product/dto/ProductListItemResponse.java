package com.sparta.team4.commerce_payment_system.domain.product.dto;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductListItemResponse {

    private final Long id;
    private final String name;
    private final String category;
    private final int price;
    private final int stockQuantity;

    public ProductListItemResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
    }
}