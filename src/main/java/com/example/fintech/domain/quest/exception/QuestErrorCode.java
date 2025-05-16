package com.example.fintech.domain.quest.exception;

import lombok.AllArgsConstructor;
import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum QuestErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "QUEST404_1", "존재하지 않는 사용자입니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "QUEST400_2", "날짜 형식이 올바르지 않습니다. (YYYY-MM-DDTHH:MM:SS)"),
    MISSING_REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "QUEST400_1", "필수 입력값이 누락되었습니다."),
    CHILD_NOT_FOUND(HttpStatus.BAD_REQUEST, "QUEST400_3", "아이가 연결되지 않았어요!");

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