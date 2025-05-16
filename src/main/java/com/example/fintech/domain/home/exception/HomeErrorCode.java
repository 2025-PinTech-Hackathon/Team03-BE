package com.example.fintech.domain.home.exception;

import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum HomeErrorCode implements BaseErrorCode {
    SUCCESS("COMMON200", "성공입니다.", HttpStatus.OK),
    UNAUTHORIZED("COMMON401", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    SERVER_ERROR("COMMON500", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CHILD_ACCOUNT_NOT_FOUND("HOME4041", "자녀의 계좌를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND("HOME4042", "계좌를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("HOME4043", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    CHILD_NOT_FOUND("HOME4044","자녀를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),
    AI_REQUEST_FAIL("HOME5001", "AI 요청 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus status;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(this.status)
                .isSuccess(false)
                .code(this.code)
                .message(this.message)
                .build();
    }
}