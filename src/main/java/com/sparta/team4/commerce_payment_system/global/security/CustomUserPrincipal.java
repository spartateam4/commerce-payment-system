package com.sparta.team4.commerce_payment_system.global.security;

import lombok.Getter;

@Getter
public class CustomUserPrincipal {

    private final Long memberId;
    private final String email;
    private final String role;

    public CustomUserPrincipal(Long memberId, String email, String role) {
        this.memberId = memberId;
        this.email = email;
        this.role = role;
    }
}