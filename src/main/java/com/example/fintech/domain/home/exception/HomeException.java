package com.example.fintech.domain.home.exception;

import lombok.Getter;

@Getter
public class HomeException extends RuntimeException {
    private final HomeErrorCode errorCode;

    public HomeException(HomeErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}