package com.example.fintech.global.exception;

import com.example.fintech.global.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{
    private final BaseErrorCode baseErrorCode;

    public GeneralException(BaseErrorCode baseErrorCode){
        super(baseErrorCode.getReasonHttpStatus().getMessage());
        this.baseErrorCode = baseErrorCode;
    }
}