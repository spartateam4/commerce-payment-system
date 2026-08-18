package com.sparta.team4.commerce_payment_system.domain.payment.controller;

import com.sparta.team4.commerce_payment_system.domain.payment.dto.GetPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.PaymentRequest;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CreatePaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/payments/")
@RequiredArgsConstructor
class PaymentController {
    private final PaymentService paymentService;

//    @PostMapping("/confirm")
//    public ResponseEntity<CreatePaymentResponse> pay(@Valid @RequestBody PaymentRequest request) {
//        return ResponseEntity.ok(paymentService.confirm(request));
//    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<GetPaymentResponse> read(@PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }
}
