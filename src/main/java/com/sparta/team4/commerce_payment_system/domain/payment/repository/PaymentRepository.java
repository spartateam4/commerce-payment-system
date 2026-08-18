package com.sparta.team4.commerce_payment_system.domain.payment.repository;

import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // JPA에 Order가 아직 등록 안됨
    //@Query("SELECT p.id FROM Payment p WHERE p.order.id = :orderId")
    Optional<Long> findPaymentIdByOrderId(@Param("orderId") Long orderId);

    //@Query("SELECT p.id, p.order.id FROM Payment p WHERE p.order.id IN :orderIds)
    List<Object[]> findIdsByOrderIds(@Param("orderIds") List<Long> orderIds);
}
