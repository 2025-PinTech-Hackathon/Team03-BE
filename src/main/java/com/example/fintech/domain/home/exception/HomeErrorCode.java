package com.example.fintech.domain.home.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HomeErrorCode {
    SUCCESS("COMMON200", "성공입니다.", HttpStatus.OK),
    UNAUTHORIZED("COMMON401", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    SERVER_ERROR("COMMON500", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CHILD_ACCOUNT_NOT_FOUND("HOME4041", "자녀의 계좌를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND("HOME4042", "계좌를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("HOME4043", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    AI_REQUEST_FAIL("HOME5001", "AI 요청 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    HomeErrorCode(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}