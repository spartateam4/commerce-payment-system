package com.sparta.team4.commerce_payment_system.domain.product.controller;

import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductListResponse;
import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductResponse;
import com.sparta.team4.commerce_payment_system.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ProductListResponse getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "0") int page,     // ← 1에서 0으로 (0-based)
            @RequestParam(defaultValue = "20") int size
    ) {
        return productService.getProducts(category, minPrice, maxPrice, page, size);
    }

    @GetMapping("/{productId}")
    public ProductResponse getProduct(@PathVariable Long productId) {
        return productService.getProduct(productId);
    }
}