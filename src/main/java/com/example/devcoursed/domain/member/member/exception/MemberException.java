package com.example.devcoursed.domain.member.member.exception;

import org.springframework.http.HttpStatus;

public enum MemberException {
    MEMBER_NOT_FOUND("존재하지 않는 ID입니다.", HttpStatus.NOT_FOUND),
    MEMBER_NOT_REGISTERED("회원가입에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_MODIFIED("회원정보 수정에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_LOGIN_DENIED("로그인에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MEMBER_NOT_REMOVED("회원정보 삭제에 실패했습니다", HttpStatus.BAD_REQUEST),
    MEMBER_IMAGE_NOT_MODIFIED("이미지 변경에 실패했습니다", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus status;

    MemberException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public MemberTaskException getMemberTaskException() {
        return new MemberTaskException(this.message,this.status.value());
    }
}
