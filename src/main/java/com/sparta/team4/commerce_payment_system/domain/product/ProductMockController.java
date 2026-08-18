package com.sparta.team4.commerce_payment_system.domain.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductMockController {

    // TODO: 실제 Product API로 교체할 임시 스텁

    @GetMapping
    public ResponseEntity<ProductListResponse> getProducts() {
        List<ProductItem> products = List.of(
                new ProductItem(1L, "물복", 1000, 20, "FOOD"),
                new ProductItem(2L, "딱복", 2000, 5, "FOOD"),
                new ProductItem(3L, "초콜릿", 3000, 15, "FOOD")
        );

        ProductListResponse response = new ProductListResponse(products, 0, 10);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id) {
        // TODO: 실제로는 id에 따라 다른 상품 반환
        ProductDetailResponse response = new ProductDetailResponse(
                id, "딱복", 2000, 20, "FOOD", "달콤하고 맛있는 딱복입니다."
        );
        return ResponseEntity.ok(response);
    }
}
