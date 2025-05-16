package com.example.fintech.global.exception;

import com.example.fintech.global.code.BaseErrorCode;
import lombok.Getter;

@Getter
public class GeneralException extends RuntimeException{
    private final BaseErrorCode baseErrorCode;
    private final Object result;

    public GeneralException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode.getReasonHttpStatus().getMessage());
        this.baseErrorCode = baseErrorCode;
        this.result = null;
    }

    public GeneralException(BaseErrorCode baseErrorCode, Object result){
        super(baseErrorCode.getReasonHttpStatus().getMessage());
        this.baseErrorCode = baseErrorCode;
        this.result = result;

    }

    public BaseErrorCode getBaseErrorCode() {
        return baseErrorCode;
    }

    public Object getResult() {
        return result;
    }
}