package com.example.devcoursed.domain.member.repository;

import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

//@Transactional(propagation = Propagation.REQUIRED)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("삽입 테스트")
    @org.junit.jupiter.api.Order(1)
    @Rollback(value = false)
    public void testInsert() {
        //GIVENa
        Member member = Member.builder()
                .loginId("1112")
                .pw("1111")
                .name("tester")
                .mImage("테스트 이미지")
                .build();

        //WHEN
        Member savedMember = memberRepository.save(member);

        //THEN
        assertNotNull(savedMember);
        assertEquals("1112", savedMember.getLoginId());
        assertEquals("1111", savedMember.getPw());
        assertEquals("tester", savedMember.getName());
        assertEquals("테스트 이미지",savedMember.getMImage());
        assertNotNull(savedMember.getProductList());
        assertNotNull(savedMember.getOrdersList());

//        log.info(savedMember);
    }

    @Test
    @DisplayName("수정 테스트")
    @Transactional
    @Commit
    @org.junit.jupiter.api.Order(2)
    public void testUpdate() {
        Long id = 1L;

        Optional<Member> foundMember = memberRepository.findById(id);
        assertTrue(foundMember.isPresent(), "Member should be present");

        Member member = foundMember.get();
        member.changeLoginId("2222");
        member.changePw("2222");
        member.changeName("is changed");
        member.changeMImage("changed title");


        assertEquals("2222", member.getLoginId(), "제품명 일치하지 않음");
        assertEquals("2222", member.getPw(), "제품명 일치하지 않음");
        assertEquals("is changed", member.getName(), "제품명 일치하지 않음");
        assertEquals("changed title", member.getMImage(), "제품명 일치하지 않음");
    }

    @Test
    @DisplayName("단일조회 테스트")
    @org.junit.jupiter.api.Order(3)
    public void testRead() {
        Long id = 1L;

        Optional<Member> foundMember = memberRepository.findById(id);
        assertTrue(foundMember.isPresent(), "Member should be present");

        Member member = foundMember.get();
        assertNotNull(member);
    }

    @Test
    @DisplayName("삭제 테스트")
    @Commit
    @org.junit.jupiter.api.Order(4)
    public void testDelete() {
        //GIVEN
        Long id = 1L;

        //WHEN
        assertTrue(memberRepository.existsById(id));
        memberRepository.deleteById(id);

        //THEN
        assertFalse(memberRepository.existsById(id));
    }




}