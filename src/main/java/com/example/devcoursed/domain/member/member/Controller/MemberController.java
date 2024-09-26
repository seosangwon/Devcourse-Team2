package com.example.devcoursed.domain.member.member.Controller;

import com.example.devcoursed.domain.member.member.dto.MemberDTO;
import com.example.devcoursed.domain.member.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
@Log4j2
public class MemberController {
    private final MemberService memberService;

    // 주문하기
    @PostMapping("/register")
    public ResponseEntity<MemberDTO.Create> register(@Validated @RequestBody MemberDTO.Create dto) {
        return ResponseEntity.ok(memberService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO.Response> read(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.read(id));
    }

    // 주문 수정하기
    @PutMapping("/{id}")
    public ResponseEntity<MemberDTO.Update> modify(@PathVariable Long id,
                                                   @Validated @RequestBody MemberDTO.Update dto) {
        dto.setId(id);
        return ResponseEntity.ok(memberService.update(dto));
    }

    // 주문 삭제하기
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.ok(Map.of("result", "success"));
    }

    // 이미지 업로드
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(@PathVariable Long id,
                                              @RequestParam("file") MultipartFile file) {
        // 업로드 디렉토리 설정
        File uploadDir = new File(System.getProperty("user.dir") + "/uploads");
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 디렉토리 생성
        }

        // 파일 저장 경로 설정
        String filePath = uploadDir + "/" + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath)); // 파일 저장
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }


        // 이미지 파일 경로를 데이터베이스에 저장
        MemberDTO.ChangeImage changeImageDto = new MemberDTO.ChangeImage();
        changeImageDto.setId(id);
        changeImageDto.setMImage(file.getOriginalFilename()); // 파일 이름을 mImage로 설정

        // 데이터베이스에 이미지 정보 저장
        memberService.changeImage(changeImageDto);

        return ResponseEntity.ok("파일 업로드 성공");
    }


    @PutMapping("/{id}/update-image")
    public ResponseEntity<MemberDTO.ChangeImage> modifyImage(@PathVariable Long id,
                                                             @Validated @RequestBody MemberDTO.ChangeImage dto) {
        dto.setId(id);
        return ResponseEntity.ok(memberService.changeImage(dto));
    }
}
