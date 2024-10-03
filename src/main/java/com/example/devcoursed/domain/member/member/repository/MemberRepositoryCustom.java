package com.example.devcoursed.domain.member.member.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {

    Page<Member> searchMembers(Pageable pageable);

}
