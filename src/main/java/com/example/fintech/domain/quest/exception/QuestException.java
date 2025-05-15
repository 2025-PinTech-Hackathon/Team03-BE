package com.example.fintech.domain.quest.exception;

import lombok.Getter;
import com.example.fintech.global.code.BaseErrorCode;

@Getter
public class QuestException extends RuntimeException {
    private final BaseErrorCode errorCode;

    public QuestException(BaseErrorCode errorCode) {
        super(errorCode.getReasonHttpStatus().getMessage());
        this.errorCode = errorCode;
    }
}
