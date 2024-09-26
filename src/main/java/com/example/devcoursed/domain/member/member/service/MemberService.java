package com.example.devcoursed.domain.member.member.service;

import com.example.devcoursed.domain.member.member.Exception.MemberException;
import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private MemberRepository memberRepository;

    @Transactional
    public MemberDTO.Create create(MemberDTO.Create dto) {
        try {
            memberRepository.save(dto.toEntity());
            return dto;
        } catch (Exception e) {
            throw MemberException.MEMBER_NOT_REGISTERED.getMemberTaskException();
        }

    }

    @Transactional
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
            throw MemberException.MEMBER_NOT_MODIFIED.getMemberTaskException();
        }
    }

    @Transactional
    public void delete(Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            memberRepository.delete(member);
        } else {
            throw MemberException.MEMBER_NOT_REMOVED.getMemberTaskException();
        }
    }

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
            throw MemberException.MEMBER_NOT_REMOVED.getMemberTaskException();
        }
    }

}
