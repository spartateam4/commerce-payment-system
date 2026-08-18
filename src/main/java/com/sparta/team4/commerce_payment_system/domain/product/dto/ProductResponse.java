package com.sparta.team4.commerce_payment_system.domain.product.dto;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private String category;
    private int totalPrice;
    private int stock;
    private String description;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.category = product.getCategory();
        this.totalPrice = product.getTotalPrice();
        this.stock = product.getStock();
        this.description = product.getDescription();
    }
}