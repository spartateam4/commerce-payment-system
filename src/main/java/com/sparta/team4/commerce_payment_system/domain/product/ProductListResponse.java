package com.sparta.team4.commerce_payment_system.domain.product;

import java.util.List;
import lombok.Getter;

@Getter
public class ProductListResponse {
    private List<ProductItem> content;
    private int page;
    private int size;
    private int totalElements;
    private int totalPages;

    public ProductListResponse(List<ProductItem> content, int page, int size) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = content.size();
        this.totalPages = 1;
    }
}
