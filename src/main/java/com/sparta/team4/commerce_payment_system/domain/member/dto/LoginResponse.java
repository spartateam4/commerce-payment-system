package com.sparta.team4.commerce_payment_system.domain.member.dto;

public record LoginResponse (
        String tokenType,
        String accessToken
) {
    public static LoginResponse of(String accessToken) {
        return new LoginResponse("Bearer", accessToken);
    }
}
