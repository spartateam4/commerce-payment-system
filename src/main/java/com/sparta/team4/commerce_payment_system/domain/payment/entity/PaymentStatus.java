package com.sparta.team4.commerce_payment_system.domain.payment.entity;

public enum PaymentStatus {
    // Payment 도메인 내에서만 가능한 상태 전이 구현이
    PENDING {
        @Override
        public boolean canTransitTo(PaymentStatus target) {
            return target == COMPLETED || target == FAILED;
        }
    },
    COMPLETED {
        @Override
        public boolean canTransitTo(PaymentStatus target) {
            return target == CANCELLED;
        }
    },
    FAILED {
        @Override
        public boolean canTransitTo(PaymentStatus target) {
            return false;
        }
    },
    CANCELLED {
        @Override
        public boolean canTransitTo(PaymentStatus target) {
            return false;
        }
    };

    public abstract boolean canTransitTo(PaymentStatus target);
}
