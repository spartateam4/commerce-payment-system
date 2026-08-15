package com.sparta.team4.commerce_payment_system.domain.member.service;

import com.sparta.team4.commerce_payment_system.domain.member.dto.LoginRequest;
import com.sparta.team4.commerce_payment_system.domain.member.dto.LoginResponse;
import com.sparta.team4.commerce_payment_system.domain.member.dto.SignupRequest;
import com.sparta.team4.commerce_payment_system.domain.member.dto.SignupResponse;
import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import com.sparta.team4.commerce_payment_system.domain.member.repository.MemberRepository;
import com.sparta.team4.commerce_payment_system.global.exception.CustomException;
import com.sparta.team4.commerce_payment_system.global.exception.ErrorCode;
import com.sparta.team4.commerce_payment_system.global.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (memberRepository.existsByEmail(request.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        Member member = Member.builder()
                .email(request.email())
                .password(encodedPassword)
                .name(request.name())
                .phone(request.phone())
                .build();

        Member saved = memberRepository.save(member);

        return SignupResponse.from(saved);  // password는 X 응답에 안 새어나감.
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        // 1. 이메일로 회원 조회 (없으면 실패)
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        // 2. 비밀번호 검증 (틀리면 실패)
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 3. 토큰 발급
        String token = jwtUtil.createToken(member.getId(), member.getEmail());

        // 4. 응답
        return LoginResponse.of(token);
    }
}
