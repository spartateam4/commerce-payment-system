package com.sparta.team4.commerce_payment_system.domain.product.dto;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String category;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.stockQuantity = product.getStockQuantity();
        this.category = product.getCategory();
        this.description = product.getDescription();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }
}