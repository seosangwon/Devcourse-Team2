package com.example.devcoursed.domain.member.member.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom {

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findByRefreshToken(String refreshToken);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByLoginIdAndEmail(String loginId , String email);
}
