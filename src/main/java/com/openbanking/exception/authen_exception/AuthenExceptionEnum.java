package com.openbanking.exception.authen_exception;

import com.openbanking.comon.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum AuthenExceptionEnum implements ErrorCode {
    AUTH_TOK_ERROR(HttpStatus.UNAUTHORIZED, "Invalid token", "AUTH_TOKEN_001"),
    AUTH_REF_ERROR(HttpStatus.UNAUTHORIZED, "Invalid refresh token", "AUTH_TOKEN_002"),
    AUTH_SIG_ERROR(HttpStatus.UNAUTHORIZED, "Invalid Signature token", "AUTH_TOKEN_003"),
    AUTH_IVD_ERROR(HttpStatus.UNAUTHORIZED, "Invalid username or password", "AUTH_001"),
    AUTH_CHECK_ERROR(HttpStatus.UNAUTHORIZED, "Username or password incorrect", "AUTH_002"),
    AUTH_LOG_ERROR(HttpStatus.UNAUTHORIZED, "Login failed", "AUTH_003");

    private final HttpStatus status;
    @Getter
    private final String message;
    @Getter
    private final String code;

    AuthenExceptionEnum(HttpStatus status, String message, String code) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

}



