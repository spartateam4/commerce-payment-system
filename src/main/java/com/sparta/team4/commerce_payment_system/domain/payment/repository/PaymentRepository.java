package com.sparta.team4.commerce_payment_system.domain.payment.repository;

import com.sparta.team4.commerce_payment_system.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p JOIN FETCH p.order o JOIN FETCH o.member WHERE p.id = :paymentId")
    Optional<Payment> findByIdWithOrderAndMember(@Param("paymentId") Long paymentId);
}
