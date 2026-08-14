package com.sparta.team4.commerce_payment_system.domain.payment.controller;

import com.sparta.team4.commerce_payment_system.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/payments/")
@RequiredArgsConstructor
class PaymentController {
    private final PaymentService paymentService;
}
