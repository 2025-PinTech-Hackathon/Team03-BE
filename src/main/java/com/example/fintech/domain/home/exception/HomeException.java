package com.example.fintech.domain.home.exception;

import com.example.fintech.global.code.BaseErrorCode;
import com.example.fintech.global.exception.GeneralException;
import lombok.Getter;

@Getter
public class HomeException extends GeneralException {
    public HomeException(BaseErrorCode baseErrorCode) {
        super(baseErrorCode);
    }
}