package com.sparta.team4.commerce_payment_system.domain.product.service;

import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductCreateRequest;
import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductResponse;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 상세 조회
    public ProductResponse getProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        return new ProductResponse(product);
    }

    // 상품 등록
    public ProductResponse createProduct(ProductCreateRequest request) {

        Product product = new Product(
                request.getName(),
                request.getCategory(),
                request.getTotalPrice(),
                request.getStock(),
                request.getDescription()
        );

        Product savedProduct = productRepository.save(product);

        return new ProductResponse(savedProduct);
    }
}