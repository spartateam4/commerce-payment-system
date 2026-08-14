package com.sparta.team4.commerce_payment_system.domain.member.repository;

import com.sparta.team4.commerce_payment_system.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}