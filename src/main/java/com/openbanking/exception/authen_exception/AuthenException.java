package com.openbanking.exception.authen_exception;

import org.springframework.http.HttpStatus;

public class AuthenException extends RuntimeException {
    private final HttpStatus status;
    private final String code;

    public AuthenException(AuthenExceptionEnum authenExceptionEnum , String message) {
        super(constructMessage(authenExceptionEnum.getMessage(), message));
        this.status = authenExceptionEnum.getHttpStatus();
        this.code = authenExceptionEnum.getCode();
    }

    private static String constructMessage(String defaultMessage, String customMessage) {
        if (customMessage != null && !customMessage.isEmpty()) {
            return defaultMessage + " " + customMessage;
        } else {
            return defaultMessage;
        }
    }
    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}