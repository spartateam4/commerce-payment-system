package com.sparta.team4.commerce_payment_system.domain.product.repository;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
        extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}