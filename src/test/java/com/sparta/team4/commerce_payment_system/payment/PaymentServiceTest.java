package com.sparta.team4.commerce_payment_system.payment;

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.order.entity.Order;
import com.sparta.team4.commerce_payment_system.domain.order.entity.OrderStatus;
import com.sparta.team4.commerce_payment_system.domain.order.repository.OrderRepository;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CancelPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.CreatePaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.GetPaymentResponse;
import com.sparta.team4.commerce_payment_system.domain.payment.dto.PaymentRequest;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import com.sparta.team4.commerce_payment_system.domain.payment.entity.PaymentStatus;
import com.sparta.team4.commerce_payment_system.domain.payment.repository.PaymentRepository;
import com.sparta.team4.commerce_payment_system.domain.payment.service.PaymentService;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    PaymentService paymentService;

    private Member member;
    private List<Order> orders;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);

        orders = List.of(
                new Order(member, "191", 4000, OrderStatus.PAYMENT_PENDING),
                new Order(member, "192", 5500, OrderStatus.PAYMENT_PENDING),
                new Order(member, "193", 14300, OrderStatus.COMPLETED),
                new Order(member, "194", 23300, OrderStatus.PAYMENT_PENDING),
                new Order(member, "195", 7100, OrderStatus.CANCELLED),
                new Order(member, "196", 12000, OrderStatus.PAYMENT_PENDING),
                new Order(member, "197", 11700, OrderStatus.COMPLETED),
                new Order(member, "198", 8900, OrderStatus.COMPLETED),
                new Order(member, "199", 15000, OrderStatus.PAYMENT_PENDING)
        );
    }

    /**
     * 정상 흐름
     */

    @Test
    @DisplayName("결제 성공: 결제·주문 상태 COMPLETED")
    void completed_pay() {
        // Given
        Order order = orders.get(3); // 주문 번호: 194

        when(member.getId()).thenReturn(1L);
        when(orderRepository.findById(4L)).thenReturn(Optional.of(order));

        // 찾은 주문데이터를 기반으로 임시 결제엔티티 생성
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentRequest request = new PaymentRequest(4L, "SUCCESS", 23300);

        // When
        CreatePaymentResponse response = paymentService.confirm(1L, request);

        // Then
        assertAll(
                () -> assertEquals(23300, response.amount()),
                () -> assertEquals(PaymentStatus.COMPLETED, response.paymentStatus()),
                () -> assertEquals(OrderStatus.COMPLETED, response.orderStatus()),
                () -> assertNotNull(response.paidAt())
        );

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("결제 실패: 결제 FAILED, 주문 CANCELLED")
    void failed_pay() {
        // Given
        Order order = orders.get(5); // 주문 번호: 196

        when(member.getId()).thenReturn(1L);
        when(orderRepository.findById(6L)).thenReturn(Optional.of(order));

        // 찾은 주문데이터를 기반으로 임시 결제엔티티 생성
        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PaymentRequest request = new PaymentRequest(6L, "FAIL", 12000);

        // When
        CreatePaymentResponse response = paymentService.confirm(1L, request);

        // Then
        assertAll(
                () -> assertEquals(12000, response.amount()),
                () -> assertEquals(PaymentStatus.FAILED, response.paymentStatus()),
                () -> assertEquals(OrderStatus.CANCELLED, response.orderStatus()),
                () -> assertNull(response.paidAt())
        );

        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("결제 내역 단 건 조회 성공")
    void find_payment() {
        // Given
        Payment payment = new Payment(orders.get(6), 11700); // 주문 번호: 197
        payment.complete(); // 억지로 결제시각 갱신
        LocalDateTime paidAt = payment.getPaidAt();

        when(member.getId()).thenReturn(1L);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // When
        GetPaymentResponse response = paymentService.get(1L, 1L);

        // Then
        assertAll(
                () -> assertEquals(11700, response.amount()),
                () -> assertEquals(PaymentStatus.COMPLETED, response.status()),
                () -> assertEquals(paidAt, response.paidAt())
        );

        verify(paymentRepository).findById(1L);
    }

    @Test
    @DisplayName("완료된 결제 취소 성공")
    void cancelled_pay() {
        // Given
        Order order = orders.get(7); // 주문 번호: 198
        ReflectionTestUtils.setField(order, "id", 8L);

        Payment payment = new Payment(order, 8900);
        ReflectionTestUtils.setField(payment, "id", 1L);
        payment.complete(); // 억지로 PaymentStatus.COMPLETED 상태로 만들기

        when(member.getId()).thenReturn(1L);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // When
        CancelPaymentResponse response = paymentService.cancel(1L, 1L);

        // Then
        assertAll(
                () -> assertEquals(1L, response.paymentId()),
                () -> assertEquals(8L, response.orderId()),
                () -> assertEquals(8900, response.amount()),
                () -> assertEquals(PaymentStatus.CANCELLED, response.paymentStatus()),
                () -> assertEquals(OrderStatus.CANCELLED, response.orderStatus())
        );

        verify(paymentRepository).findById(1L);
    }

    /**
     * 예외 흐름
      */

    @Test
    @DisplayName("주문 소유자 불일치: ACCESS_DENIED")
    void access_denied_403() {
        // Given
        Order order = orders.get(0); // 주문 번호: 191

        when(member.getId()).thenReturn(1L); // 실제 주문 소유자
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        PaymentRequest request = new PaymentRequest(1L, "SUCCESS", 4000);

        // When & Then
        CustomException exception = assertThrows(CustomException.class,
                () -> paymentService.confirm(2L, request)); // 다른 요청자

        assertEquals(ErrorCode.ACCESS_DENIED, exception.getErrorCode());
    }

    @Test
    @DisplayName("결제 금액 불일치: PRICE_MISMATCH")
    void price_mismatch_400() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("존재하지 않는 주문·결제")
    void payment_or_order_not_found_404() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("잘못된 상태에서 결제 실패")
    void illegal_status_pay_409() {
        // Given
        // When
        // Then
    }

    @Test
    @DisplayName("잘못된 상태에서 결제 취소 실패")
    void illegal_status_cancel_409() {
        // Given
        // When
        // Then
    }
}