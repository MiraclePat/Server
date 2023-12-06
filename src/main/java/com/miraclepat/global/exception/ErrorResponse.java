package com.miraclepat.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final int code;
    private final String errorMessage;

    protected ErrorResponse(ErrorCode errorCode, String errorMessage) {
        this.code = errorCode.getCode();
        this.errorMessage = errorMessage;
    }

    private ErrorResponse(int code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public static ErrorResponse of(ErrorCode errorCode, String errorMessage) {
        return new ErrorResponse(errorCode.getCode(), errorMessage != null ? errorMessage : errorCode.getMessage());
    }

}
