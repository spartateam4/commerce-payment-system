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

        Long memberId = principal.getMemberId();

        CartItemResponse response = cartService.addItem(
                memberId,
                request.productId(),
                request.quantity()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    public ResponseEntity<CartResponse> getCart(
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        Long memberId = principal.getMemberId();
        CartResponse response = cartService.getCart(memberId);

        return ResponseEntity.ok(response);
    }


    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartRequest request) {

        Long memberId = principal.getMemberId();
        CartItemResponse response = cartService.updateCartItem(
                memberId,
                cartItemId,
                request.quantity()
        );
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long cartItemId) {

        Long memberId = principal.getMemberId();
        cartService.deleteCartItem(memberId, cartItemId);

        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteAllCartItems(
            @AuthenticationPrincipal CustomUserPrincipal principal) {

        Long memberId = principal.getMemberId();
        cartService.deleteAllCartItems(memberId);

        return ResponseEntity.noContent().build();
    }
}
