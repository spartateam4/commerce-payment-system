package com.sparta.team4.commerce_payment_system.domain.product.repository;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {

    Page<Product> searchProducts(
            String category,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable
    );
}