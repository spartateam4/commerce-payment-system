package com.sparta.team4.commerce_payment_system.domain.payment.entity;

public enum PaymentStatus {
    PENDING { // 대기
        @Override
        public boolean canTransitTo(PaymentStatus target) {
            return target == PaymentStatus.COMPLETED || target == PaymentStatus.FAILED;
        }
    },
    COMPLETED { // 결제 성공
        @Override
        public boolean canTransitTo(PaymentStatus target) {
            return target == PaymentStatus.CANCELLED;
        }
    },
    FAILED { // 결제 실패
        @Override
        public boolean canTransitTo(PaymentStatus target) {
            return false;
        }
    },
    CANCELLED { // 결제 취소
        @Override
        public boolean canTransitTo(PaymentStatus target) {
            return false;
        }
    };

    public abstract boolean canTransitTo(PaymentStatus target);
}
