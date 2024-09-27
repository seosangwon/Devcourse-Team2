package com.example.devcoursed.domain.member.member.controller;


import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.service.MemberService;
import com.example.devcoursed.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
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



//    @GetMapping("/login")
//    public ResponseEntity<?>

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















