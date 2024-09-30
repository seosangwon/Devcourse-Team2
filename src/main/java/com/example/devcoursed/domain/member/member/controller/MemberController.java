package com.example.devcoursed.domain.member.member.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.global.security.SecurityUser;
import com.example.devcoursed.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
@Log4j2
public class MemberController {
    private final MemberService memberService;
    private final ResourceLoader resourceLoader;

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<MemberDTO.Create> register(@Validated @RequestBody MemberDTO.Create dto) {
        return ResponseEntity.ok(memberService.create(dto));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<MemberDTO.LoginResponseDto> logins(@Validated @RequestBody MemberDTO.LoginRequestDto request) {
        //인증 성공
        MemberDTO.LoginResponseDto responseDto = memberService.checkLoginIdAndPassword(request.getLoginId(), request.getPw());

        Long id = responseDto.getId();
        String loginId = responseDto.getLoginId();

        List<String> authorities;
        if (request.getLoginId().equals("admin")) {
            authorities = List.of("ROLE_ADMIN");
        } else {
            authorities = List.of("ROLE_MEMBER");
        }

        String accessToken = JwtUtil.encode(
                Map.of("id", id.toString(),
                        "loginId", loginId,
                        "authorities", authorities)
        );

        responseDto.setAccessToken(accessToken);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/")
    public ResponseEntity<MemberDTO.Response> read(@AuthenticationPrincipal SecurityUser user) {
        long id = user.getId();
        return ResponseEntity.ok(memberService.read(id));
    }

    //주문 수정하기
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
}
