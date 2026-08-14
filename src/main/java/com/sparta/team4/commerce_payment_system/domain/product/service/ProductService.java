package com.sparta.team4.commerce_payment_system.domain.product.service;

import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductResponse;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse getProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        return new ProductResponse(product);
    }
}