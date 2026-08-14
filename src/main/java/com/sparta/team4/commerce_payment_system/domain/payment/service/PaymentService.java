package com.sparta.team4.commerce_payment_system.domain.payment.service;

import com.sparta.team4.commerce_payment_system.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
}
