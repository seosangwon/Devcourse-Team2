package com.example.devcoursed.domain.member.member.controller;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.global.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
@Log4j2
public class MemberController {
    private final MemberService memberService;
    private final ResourceLoader resourceLoader;
    private final JavaMailSender mailSender;

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<MemberDTO.CreateResponseDto> register(@Validated @RequestBody MemberDTO.CreateRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(error -> {
                        if (error instanceof FieldError) {
                            return (error.getDefaultMessage());
                        } else {
                            return error.getDefaultMessage();
                        }
                    })
                    .collect(Collectors.joining(", "));


            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MemberDTO.CreateResponseDto(errorMessage));
        }

        MemberDTO.CreateResponseDto createResponseDto = memberService.create(request);


        return ResponseEntity.ok(createResponseDto);
    }


    //로그인
    @PostMapping("/login")
    public ResponseEntity<MemberDTO.LoginResponseDto> login(@Validated @RequestBody MemberDTO.LoginRequestDto request ) {
        //인증 성공
        MemberDTO.LoginResponseDto responseDto = memberService.checkLoginIdAndPassword(request.getLoginId(), request.getPw());

        Long id = responseDto.getId();
        String loginId = responseDto.getLoginId();

        String accessToken = memberService.generateAccessToken(id, loginId);
        String refreshToken = memberService.generateRefreshToken(id, loginId);

        memberService.setRefreshToken(id, refreshToken);

        responseDto.setAccessToken(accessToken);
        responseDto.setRefreshToken(refreshToken);

        return ResponseEntity.ok(responseDto);


    }

    @PostMapping("/refreshAccessToken")
    public ResponseEntity<MemberDTO.RefreshAccessTokenResponseDto> login(@RequestBody MemberDTO.RefreshAccessTokenRequestDto request) {
        String accessToken = memberService.refreshAccessToken(request.getRefreshToken());
        MemberDTO.RefreshAccessTokenResponseDto responseDto = new MemberDTO.RefreshAccessTokenResponseDto(accessToken, "새로운 AccessToken 발급");

        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/logout")
    public ResponseEntity<MemberDTO.logoutResponseDto> logout(@AuthenticationPrincipal SecurityUser user) {
        memberService.setRefreshToken(user.getId(), "null");

        return ResponseEntity.ok(new MemberDTO.logoutResponseDto("로그아웃 되었습니다"));

    }


    // 나의 회원 정보 조회화기
    @GetMapping("/")
    public ResponseEntity<MemberDTO.Response> getMyPage(@AuthenticationPrincipal SecurityUser user) {
        long id = user.getId();
        return ResponseEntity.ok(memberService.read(id));
    }

    //다른 유저의 회원정보 조회하기
    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO.Response> read(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.read(id));
    }


    //내 정보 수정하기
    @PutMapping("/")
    public ResponseEntity<MemberDTO.Update> modify(@AuthenticationPrincipal SecurityUser user,
                                                   @Validated @RequestBody MemberDTO.Update dto) {
        long id = user.getId();
        dto.setId(id);
        return ResponseEntity.ok(memberService.update(dto));
    }

    //주문 삭제하기
    @DeleteMapping("/")
    public ResponseEntity<Map<String, String>> delete(@AuthenticationPrincipal SecurityUser user) {
        long id = user.getId();
        memberService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/update-image")
    public ResponseEntity<MemberDTO.ChangeImage> modifyImage(
            @AuthenticationPrincipal SecurityUser user,
            @RequestParam("mImage") MultipartFile mImage) {
        MemberDTO.ChangeImage dto = new MemberDTO.ChangeImage();
        dto.setId(user.getId());
        dto.setMImage(mImage);
        return ResponseEntity.ok(memberService.changeImage(dto, mImage));
    }

    // 이미지 서빙 엔드포인트
    @GetMapping("/upload/{filename:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable String filename) {
        Resource file = resourceLoader.getResource("file:upload/" + filename);
        return ResponseEntity.ok(file);
    }

    //아이디 찾기
    @GetMapping("/findId")
    public ResponseEntity<String> findId(@RequestParam String email) {
        String loginId = memberService.findByEmail(email);

        return ResponseEntity.ok(loginId);
    }

    //비밀번호 찾기
    @PostMapping("/findPW")
    public ResponseEntity<String> findPw(@RequestBody MemberDTO.FindPWRequestDto request) {
        String templatePassword = memberService.setTemplatePassword(request.getLoginId(), request.getEmail());

        String title = "데브코스 팀2 아이디/비밀번호 찾기 인증 이메일 입니다.";
        String from= "seodo1e1205@gmail.com";
        String to = request.getEmail();
        String content =
                System.getProperty("line.separator")+
                        System.getProperty("line.separator")+
                        "임시 비밀번호로 로그인 후 꼭 새로운 비밀번호로 설정해주시기 바랍니다."
                        +System.getProperty("line.separator")+
                        System.getProperty("line.separator")+
                        "임시비밀번호는 " +templatePassword+ " 입니다. "
                        +System.getProperty("line.separator");

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(title);
            messageHelper.setText(content);

            mailSender.send(message);

        } catch (Exception e) {
            log.debug(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("임시 비밀번호 전송을 실패하였습니다" );
        }
        return ResponseEntity.ok("임시 비밀번호를 이메일로 전송했습니다");


    }



}
