package com.sparta.team4.commerce_payment_system.domain.payment.service;

import com.sparta.team4.commerce_payment_system.domain.payment.dto.GetPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
// TODO order엔티티 연동 필요
public class PaymentService {
    private final PaymentRepository paymentRepository;
    //private final OrderRepository orderRepository;

//    @Transactional
//    public PaymentResponse confirm(PaymentRequest request) {
//        // 요청 ID에 해당하는 주문데이터(행) 찾기
//        //Order targetOrder = orderRepository.findById(request.orderId()).orElseThrow();
//
//        // TODO 주문 소유자 확인
//
//        // 찾은 주문데이터를 기반으로 임시 결제엔티티 생성
//        //Payment payment = new Payment(targetOrder, targetOrder.getTotalAmount());
//
//        // 주문 상태가 "결제대기"가 아님
//        //if (targetOrder.getStatus() != OrderStatus.PAYMENT_PENDING)
//        //    throw new RuntimeException();
//
//        // 요청금액과 주문금액 불일치 (금액 위조 방지)
//        //if (!request.amount().equals(targetOrder.getTotalAmount()))
//        //    throw new RuntimeException();
//
//        // 결과에 따라 분기
//        switch (request.result()) {
//            case "SUCCESS" -> {
//                payment.complete();
//                // TODO 주문상태: COMPLETED
//                // TODO 장바구니 비우기 - cart.clear()
//            }
//            case "FAIL" -> {
//                payment.fail();
//                // TODO 주문상태: CANCELLED
//                // TODO 재고 복구 - product.stock() + order.stock()
//            }
//            default -> throw new RuntimeException();
//        }
//
//        // DB에 저장
//        Payment savePayment = paymentRepository.save(payment);
//
//        return PaymentResponse.from(savePayment);
//    }

    @Transactional(readOnly = true)
    public GetPaymentResponse getPayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();

        return GetPaymentResponse.from(payment);
    }
}
