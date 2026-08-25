package com.sparta.team4.commerce_payment_system.domain.product.dto;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class ProductListResponse {

    private final List<ProductListItemResponse> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;

    public ProductListResponse(Page<Product> productPage) {
        this.content = productPage.getContent().stream()
                .map(ProductListItemResponse::new)
                .toList();
        this.page = productPage.getNumber();
        this.size = productPage.getSize();
        this.totalElements = productPage.getTotalElements();
        this.totalPages = productPage.getTotalPages();
    }
}