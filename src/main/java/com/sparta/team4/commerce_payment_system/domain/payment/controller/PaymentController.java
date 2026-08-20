package com.sparta.team4.commerce_payment_system.domain.payment.controller;

import com.sparta.team4.commerce_payment_system.domain.payment.dto.CancelPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.GetPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.PaymentRequest;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CreatePaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.service.PaymentService;
import com.sparta.team4.commerce_payment_system.global.security.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<CreatePaymentResponse> confirmPayment(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.confirm(principal.getMemberId(), request));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<GetPaymentResponse> getPayment(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.get(principal.getMemberId(), paymentId));
    }

    @PatchMapping("/{paymentId}/cancel")
    public ResponseEntity<CancelPaymentResponse> cancelPayment(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.cancel(principal.getMemberId(), paymentId));
    }
}
