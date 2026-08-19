package com.sparta.team4.commerce_payment_system.domain.product.service;

import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductDetailResponse;
import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductListResponse;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.domain.product.repository.ProductRepository;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private static final int MAX_PAGE_SIZE = 100;   // 페이지 최대 조회 크기

    private final ProductRepository productRepository;

    public ProductListResponse getProducts(
            String category, Integer minPrice, Integer maxPrice, int page, int size) {

        if (page < 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_SEARCH);
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_SEARCH);
        }
        if (minPrice != null && minPrice < 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_SEARCH);
        }
        if (maxPrice != null && maxPrice < 0) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_SEARCH);
        }
        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_SEARCH);
        }

        PageRequest pageable = PageRequest.of(page, size);
        Page<Product> products =
                productRepository.searchProducts(category, minPrice, maxPrice, pageable);

        return new ProductListResponse(products);
    }

    public ProductDetailResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        return new ProductDetailResponse(product);
    }
}