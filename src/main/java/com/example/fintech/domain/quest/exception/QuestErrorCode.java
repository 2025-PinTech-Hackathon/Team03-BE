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
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "QUEST404_3", "해당 카테고리를 찾을 수 없습니다."),
    QUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "QUEST404_1", "해당 퀘스트를 찾을 수 없습니다."),
    MONEY_TRANSFER_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MONEY500_1", "보상 금액 이체를 실패했습니다."),
    CHILD_NOT_FOUND(HttpStatus.NOT_FOUND, "QUEST404_2", "자녀 정보를 찾을 수 없습니다."),
    INVALID_ROLE(HttpStatus.FORBIDDEN, "QUEST403_1", "권한이 올바르지 않습니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "QUEST401_1", "인증 정보가 없습니다. 토큰을 포함해주세요."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "QUEST404_3", "자녀 계좌를 찾을 수 없습니다."),
    PARENT_NOT_FOUND(HttpStatus.NOT_FOUND, "QUEST404_4", "부모 정보를 찾을 수 없습니다.");;

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