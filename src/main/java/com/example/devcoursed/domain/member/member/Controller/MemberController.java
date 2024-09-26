package com.example.devcoursed.domain.member.member.Controller;


import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.service.MemberService;
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

    //주문하기
    @PostMapping("")
    public ResponseEntity<MemberDTO.Create> register(@Validated @RequestBody MemberDTO.Create dto) {
        return ResponseEntity.ok(memberService.create(dto));
    }

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
}















