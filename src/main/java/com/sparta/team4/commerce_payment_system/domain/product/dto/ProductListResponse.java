package com.sparta.team4.commerce_payment_system.domain.product.dto;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductListResponse {

    private List<ProductResponse> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public ProductListResponse(
            List<ProductResponse> content,
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }
}