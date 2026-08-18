package com.sparta.team4.commerce_payment_system.domain.product.service;

import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductListResponse;
import com.sparta.team4.commerce_payment_system.domain.product.dto.ProductResponse;
import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductListResponse getProducts(
            String category,
            Integer minPrice,
            Integer maxPrice,
            int page,
            int size
    ) {
        if (page < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "페이지 번호는 1 이상이어야 합니다."
            );
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "페이지 크기는 1~100 사이여야 합니다."
            );
        }

        if (minPrice != null
                && maxPrice != null
                && minPrice > maxPrice) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "최소 가격은 최대 가격보다 클 수 없습니다."
            );
        }

        PageRequest pageable = PageRequest.of(page - 1, size);

        Page<Product> products = productRepository.searchProducts(
                category,
                minPrice,
                maxPrice,
                pageable
        );

        List<ProductResponse> content = products.getContent()
                .stream()
                .map(ProductResponse::new)
                .toList();

        return new ProductListResponse(
                content,
                page,
                size,
                products.getTotalElements(),
                products.getTotalPages()
        );
    }

    public ProductResponse getProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "상품을 찾을 수 없습니다."
                ));

        return new ProductResponse(product);
    }
}