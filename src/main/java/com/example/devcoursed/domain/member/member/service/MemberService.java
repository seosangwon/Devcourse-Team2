package com.example.devcoursed.domain.member.member.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import com.example.devcoursed.domain.member.member.exception.MemberException;
import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public MemberDTO.Create create(MemberDTO.Create dto) {
        try {
            //기존의 회원이 있는지 검사
            Optional<Member> member = memberRepository.findByLoginId(dto.getLoginId());
            if (member.isPresent()) {
                throw new RuntimeException("해당 아이디로 회원가입한 회원이 존재합니다");
            }

            String password = dto.getPw();
            dto.setPw(passwordEncoder.encode(password));

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
            String password = dto.getPw();
            dto.setPw(passwordEncoder.encode(password));
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

    private final String uploadDir = "upload/"; // 현재 디렉토리의 upload 폴더

    @Transactional
    public MemberDTO.ChangeImage changeImage(MemberDTO.ChangeImage dto, MultipartFile imageFile) { // MultipartFile 추가
        Optional<Member> memberOptional = memberRepository.findById(dto.getId());
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String fileName = saveImage(imageFile); // MultipartFile로 파일 저장

            member.changeMImage(fileName); // 파일 이름을 저장
            memberRepository.save(member);
            return new MemberDTO.ChangeImage(member.getId(), imageFile);
        } else {
            throw MemberException.MEMBER_IMAGE_NOT_MODIFIED.getMemberTaskException();
        }
    }

    private String saveImage(MultipartFile imageFile) { // MultipartFile로 변경
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // 디렉토리가 존재하지 않으면 생성
            }

            // 파일 이름 생성 및 저장
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(imageFile.getInputStream(), filePath); // 실제 파일 저장

            return fileName; // 저장된 파일 이름 반환
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    public MemberDTO.LoginResponseDto checkLoginIdAndPassword(String loginId, String pw) {
        Optional<Member> opMember = memberRepository.findByLoginId(loginId);

        if (opMember.isEmpty()) {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }

        if (!passwordEncoder.matches(pw, opMember.get().getPw())) {
            throw MemberException.MEMBER_LOGIN_DENIED.getMemberTaskException();
        }

        Member member = opMember.get();
        MemberDTO.LoginResponseDto responseDto = new MemberDTO.LoginResponseDto(member);

        return responseDto;


    }

    public int count() {
        return memberRepository.findAll().size();
    }
}
