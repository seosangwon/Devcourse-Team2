package com.example.devcoursed.domain.member.member.service;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private MemberRepository memberRepository;

    public MemberDTO.Create create(MemberDTO.Create dto) {
        memberRepository.save(dto.toEntity());
        return dto;
    }

    public MemberDTO.Update update(MemberDTO.Update dto) {
        Optional<Member> memberOptional = memberRepository.findById(dto.getId());

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.changeLoginId(dto.getLoginId());
            member.changePw(dto.getPw());
            member.changeName(dto.getName());
            member.changeMImage(dto.getMImage());
            memberRepository.save(member);

            return new MemberDTO.Update(
                    member.getId(),
                    member.getLoginId(),
                    member.getPw(),
                    member.getName(),
                    member.getMImage()
            );
        } else {
            return null; //Exception 처리 예정
        }
    }

    public void delete(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            memberRepository.delete(member);
        } else {
            //Exception 처리
        }
    }

    @Transactional(readOnly = true)
    public MemberDTO.Response read(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            return new MemberDTO.Response(
                    member.getLoginId(),
                    member.getPw(),
                    member.getName(),
                    member.getMImage(),
                    member.getCreatedAt(),
                    member.getModifiedAt()
            );
        } else {
            return null; //Exception 처리
        }
    }

}
