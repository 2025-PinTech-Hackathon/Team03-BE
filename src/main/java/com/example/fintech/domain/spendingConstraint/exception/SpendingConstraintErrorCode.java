package com.example.fintech.domain.spendingConstraint.exception;

import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public enum SpendingConstraintErrorCode implements BaseErrorCode{

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "LIMIT404_1", "존재하지 않는 사용자입니다."),
    MISSING_REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "LIMIT400_1", "필수 입력값이 누락되었습니다."),
    LIMIT_NOT_FOUND(HttpStatus.NOT_FOUND, "LIMIT404_1", "해당 퀘스트를 찾을 수 없습니다."),
    INVALID_ROLE(HttpStatus.FORBIDDEN, "LIMIT403_1", "권한이 올바르지 않습니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "LIMIT401_1", "인증 정보가 없습니다. 토큰을 포함해주세요.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(this.httpStatus)
                .isSuccess(false)
                .code(this.code)
                .message(this.message)
                .build();
    }


}