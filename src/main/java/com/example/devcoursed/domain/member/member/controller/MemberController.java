package com.example.devcoursed.domain.member.member.controller;


import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.global.security.SecurityUser;
import com.example.devcoursed.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
@Log4j2
public class MemberController {
    private final MemberService memberService;

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<MemberDTO.Create> register(@Validated @RequestBody MemberDTO.Create dto) {
        return ResponseEntity.ok(memberService.create(dto));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<MemberDTO.LoginResponseDto> login(@Validated @RequestBody MemberDTO.LoginRequestDto request) {
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
    public ResponseEntity<MemberDTO.RefreshAccessTokenResponseDto> login (@RequestBody MemberDTO.RefreshAccessTokenRequestDto request) {
        String accessToken = memberService.refreshAccessToken(request.getRefreshToken());
        MemberDTO.RefreshAccessTokenResponseDto responseDto = new MemberDTO.RefreshAccessTokenResponseDto(accessToken , "새로운 AccessToken 발급");

        return ResponseEntity.ok(responseDto);
    }


    @PostMapping("/logout")
    public ResponseEntity<MemberDTO.logoutResponseDto> logout(@AuthenticationPrincipal SecurityUser user) {
        memberService.setRefreshToken(user.getId(),"null");

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


    //주문 수정하기
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO.Update> modify(@PathVariable Long id,
                                                   @Validated @RequestBody MemberDTO.Update dto) {
        dto.setId(id);
        return ResponseEntity.ok(memberService.update(dto));
    }

    //주문 삭제하기
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    @PutMapping("/{id}/update-image")
    public ResponseEntity<MemberDTO.ChangeImage> modifyImage(@PathVariable Long id,
                                                             @Validated @RequestBody MemberDTO.ChangeImage dto) {
        dto.setId(id);
        return ResponseEntity.ok(memberService.changeImage(dto));
    }
}















