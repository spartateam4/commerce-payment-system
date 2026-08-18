package com.sparta.team4.commerce_payment_system.domain.product.controller;

import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductCreateRequest;
import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductResponse;
import com.sparta.team4.commerce_payment_system.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {

        ProductResponse response = productService.getProduct(productId);

        return ResponseEntity.ok(response);
    }

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductCreateRequest request) {

        ProductResponse response = productService.createProduct(request);

        return ResponseEntity.ok(response);
    }
}