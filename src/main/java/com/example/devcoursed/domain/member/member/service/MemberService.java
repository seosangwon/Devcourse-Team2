package com.example.devcoursed.domain.member.member.service;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.entity.Member;
import com.example.devcoursed.domain.member.member.exception.MemberException;
import com.example.devcoursed.domain.member.member.repository.MemberRepository;
import com.example.devcoursed.global.util.JwtUtil;
import com.example.devcoursed.global.util.PasswordUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jdk.jshell.execution.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public MemberDTO.CreateResponseDto create(MemberDTO.CreateRequestDto dto) {
        try {
            //기존의 회원이 있는지 검사
            Optional<Member> member = memberRepository.findByLoginId(dto.getLoginId());
            if (member.isPresent()) {
                throw new RuntimeException("해당 아이디로 회원가입한 회원이 존재합니다");
            }

            String password = dto.getPw();
            dto.setPw(passwordEncoder.encode(password));

            Member savedMember = memberRepository.save(dto.toEntity());
            return new MemberDTO.CreateResponseDto("회원가입이 완료되었습니다");
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
            member.changePw(passwordEncoder.encode(dto.getPw()));
            member.changeName(dto.getName());
            member.changeMImage(dto.getMImage());
            member.changeEmail(dto.getEmail()); // 잠시 수정
            memberRepository.save(member);

            return new MemberDTO.Update(
                    member.getId(),
                    member.getLoginId(),
                    member.getPw(),
                    member.getName(),
                    member.getMImage(),
                    member.getEmail()  // 잠시 수정
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
            return new MemberDTO.Response(member);
        } else {
            throw MemberException.MEMBER_NOT_REMOVED.getMemberTaskException();
        }
    }

    public Page<MemberDTO.Response> readAll(Pageable pageable) {
        Page<Member> members = memberRepository.searchMembers(pageable);

        return members.map(MemberDTO.Response::new);


    }

    private final String uploadDir = "upload/"; // 현재 디렉토리의 upload 폴더

    @Transactional
    public MemberDTO.ChangeImage changeImage(MemberDTO.ChangeImage dto, MultipartFile imageFile) { // MultipartFile 추가
        Optional<Member> memberOptional = memberRepository.findById(dto.getId());
        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            String fileName = saveImage(imageFile); // MultipartFile로 파일 저장

            member.changeMImage(fileName); // URL 저장
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

    public Member getMemberById(Long id) {
        Optional<Member> opMember = memberRepository.findById(id);

        if (opMember.isEmpty()) {
            throw MemberException.MEMBER_NOT_FOUND.getMemberTaskException();
        }

        return opMember.get();
    }

    public int count() {
        return memberRepository.findAll().size();
    }

    @Transactional
    public void setRefreshToken(Long id, String refreshToken) {
        Member member = memberRepository.findById(id).get();
        member.updateRefreshToken(refreshToken);

    }

    public String generateAccessToken(Long id, String loginId) {
        List<String> authorities;
        if (loginId.equals("admin")) {
            authorities = List.of("ROLE_ADMIN");
        } else {
            authorities = List.of("ROLE_MEMBER");
        }

        return JwtUtil.encodeAccessToken(15,
                Map.of("id", id.toString(),
                        "loginId", loginId,
                        "authorities", authorities)
        );


    }

    public String generateRefreshToken(Long id, String loginId) {
        return JwtUtil.encodeRefreshToken(60 * 24 * 3,
                Map.of("id", id.toString(),
                        "loginId", loginId)
        );

    }

    public String refreshAccessToken(String refreshToken) {
        //화이트리스트 처리
        Member member = memberRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> MemberException.MEMBER_LOGIN_DENIED.getMemberTaskException());

        //리프레시 토큰이 만료되었다면 로그아웃
        try {
            Claims claims = JwtUtil.decode(refreshToken); // 여기서 에러 처리가 남
        } catch (ExpiredJwtException e) {
            // 클라이언트한테 만료되었다고 알려주기
            throw MemberException.MEMBER_REFRESHTOKEN_EXPIRED.getMemberTaskException();

        }


        return generateAccessToken(member.getId(), member.getLoginId());
    }


    public String findByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> MemberException.MEMBER_NOT_FOUND.getMemberTaskException());
        return member.getLoginId();

    }

    @Transactional
    public String setTemplatePassword(String loginId, String email) {
        Member member = memberRepository.findByLoginIdAndEmail(loginId, email).orElseThrow(() -> MemberException.MEMBER_NOT_FOUND.getMemberTaskException());
        String templatePassword = PasswordUtil.generateTempPassword();
        member.changePw(passwordEncoder.encode(templatePassword));
        memberRepository.save(member);

        return templatePassword;

    }
}
