package com.sparta.team4.commerce_payment_system.domain.cart.controller;

import com.sparta.team4.commerce_payment_system.domain.cart.dto.*;
import com.sparta.team4.commerce_payment_system.domain.cart.service.CartService;
import com.sparta.team4.commerce_payment_system.global.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItem(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody AddCartRequest request) {

        System.out.println(" principal 확인: " + principal);

        if (principal == null) {
            System.out.println("⚠️ principal이 null입니다!");
            return ResponseEntity.status(401).body(null);
        }

        System.out.println(" memberId: " + principal.getMemberId());

        // Long memberId = principal.getMemberId();

        CartItemResponse response = cartService.addItem(
                principal.getMemberId(),
                request.productId(),
                request.quantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        // Long memberId = principal.getMemberId();
        CartResponse response = cartService.getCart(principal.getMemberId());

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartRequest request) {

        // Long memberId = principal.getMemberId();
        CartItemResponse response = cartService.updateCartItem(
                principal.getMemberId(),
                cartItemId,
                request.quantity()
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long cartItemId) {

        // Long memberId = principal.getMemberId();
        cartService.deleteCartItem(principal.getMemberId(), cartItemId);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteAllCartItems(
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        // Long memberId = principal.getMemberId();
        cartService.deleteAllCartItems(principal.getMemberId());

        return ResponseEntity.noContent().build();
    }
}
