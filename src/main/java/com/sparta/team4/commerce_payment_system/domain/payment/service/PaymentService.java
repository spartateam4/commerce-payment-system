package com.sparta.team4.commerce_payment_system.domain.payment.service;

import com.sparta.team4.commerce_payment_system.domain.cart.service.CartService;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderItem;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CancelPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CreatePaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.GetPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.PaymentRequest;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.repository.PaymentRepository;
import com.sparta.team4.commerce_payment_system.domain.product.service.ProductService;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ProductService productService;
    private final CartService cartService;

    /** 모의 결제 */
    @Transactional
    public CreatePaymentResponse confirm(Long requestedByMemberId, PaymentRequest request) {
        // 요청 ID에 해당하는 주문데이터(행) 찾기
        Order targetOrder = paymentRepository.findOrderByIdWithPaymentDetails(request.orderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND)); // 404

        // 주문 소유자 확인
        if (!requestedByMemberId.equals(targetOrder.getMember().getId()))
            throw new CustomException(ErrorCode.ACCESS_DENIED); // 403

        // 주문 상태가 "결제대기"가 아님
        if (targetOrder.getStatus() != OrderStatus.PAYMENT_PENDING)
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS); // 409

        // 요청금액과 주문금액 불일치 (금액 위조 방지)
        if (!request.amount().equals(targetOrder.getTotalAmount()))
            throw new CustomException(ErrorCode.PRICE_MISMATCH); // 400

        // 찾은 주문데이터를 기반으로 임시 결제엔티티 생성
        Payment payment = new Payment(targetOrder, targetOrder.getTotalAmount());

        // 결과에 따라 분기
        switch (request.result()) {
            case "SUCCESS" -> {
                payment.complete(); // 결제: PENDING -> COMPLETED
                payment.getOrder().completeOrder(); // 주문: PAYMENT_PENDING -> COMPLETED
                cartService.deleteAllCartItems(requestedByMemberId); // 장바구니 비우기
            }
            case "FAIL" -> {
                payment.fail(); // 결제: PENDING -> FAILED
                payment.getOrder().cancelOrder(); // 주문: PAYMENT_PENDING -> CANCELLED
                restoreStockAtPayment(targetOrder); // 재고 복구
            }
            default -> throw new CustomException(ErrorCode.INVALID_PAYMENT_RESULT); // 400
        }

        // DB에 저장
        Payment savedPayment = paymentRepository.save(payment);

        return CreatePaymentResponse.from(savedPayment);
    }

    /** 결제 단 건 조회 */
    @Transactional(readOnly = true)
    public GetPaymentResponse get(Long requestedByMemberId, Long paymentId) {
        // 요청 ID에 해당하는 결제데이터(행) 찾기
        Payment payment = paymentRepository.findByIdWithOrderAndMember(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND)); // 404

        validateOwner(requestedByMemberId, payment);

        return GetPaymentResponse.from(payment);
    }

    /** 결제 후 취소 (환불) */
    @Transactional
    public CancelPaymentResponse cancel(Long requestedByMemberId, Long paymentId) {
        // 요청 ID에 해당하는 결제데이터(행) 찾기
        Payment targetPayment = paymentRepository.findByIdWithOrderMemberAndItems(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND)); // 404

        validateOwner(requestedByMemberId, targetPayment);

        // 결제 상태가 "완료"가 아님
        if (targetPayment.getStatus() != PaymentStatus.COMPLETED)
            throw new CustomException(ErrorCode.INVALID_PAYMENT_STATUS); // 409

        // 주문 상태가 "결제완료"가 아님
        if (targetPayment.getOrder().getStatus() != OrderStatus.COMPLETED)
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS); // 409

        targetPayment.cancel(); // 결제: COMPLETED -> CANCELLED
        targetPayment.getOrder().cancelOrder(); // 주문: COMPLETED -> CANCELLED
        restoreStockAtPayment(targetPayment.getOrder()); // 재고 복구

        return CancelPaymentResponse.from(targetPayment);
    }

    private void restoreStockAtPayment(Order order) {
        for (OrderItem orderItem : order.getOrderItems())
            productService.restoreStock(orderItem.getProductId(), orderItem.getQuantity());
    }

    // 결제 소유자 확인
    private void validateOwner(Long requestedByMemberId, Payment payment) {
        if (!requestedByMemberId.equals(payment.getOrder().getMember().getId()))
            throw new CustomException(ErrorCode.ACCESS_DENIED); // 403
    }
}
