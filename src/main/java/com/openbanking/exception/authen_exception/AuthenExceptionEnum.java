package com.openbanking.exception.authen_exception;

import com.openbanking.comon.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum AuthenExceptionEnum implements ErrorCode {
    AUTH_TOK_ERROR(HttpStatus.UNAUTHORIZED, "The token is run out of time or corrupted", "AUTH_TOKEN_001"),
    AUTH_REF_ERROR(HttpStatus.UNAUTHORIZED, "The refresh is invalid or corrupted", "AUTH_TOKEN_002"),
    AUTH_SIG_ERROR(HttpStatus.UNAUTHORIZED, "The token signature is invalid or corrupted", "AUTH_TOKEN_003"),
    AUTH_IVD_ERROR(HttpStatus.UNAUTHORIZED, "Invalid username or password", "AUTH_001"),
    AUTH_CHECK_ERROR(HttpStatus.UNAUTHORIZED, "Username or password incorrect", "AUTH_002"),
    AUTH_LOG_ERROR(HttpStatus.UNAUTHORIZED, "Login failed", "AUTH_003"),
    AUTH_PASS_DEF_ERROR(HttpStatus.UNAUTHORIZED, "New password must be different from default password", "AUTH_PASS_001"),
    AUTH_PASS_NEW_ERROR(HttpStatus.UNAUTHORIZED, "New password is not the same as re-enter new password", "AUTH_PASS_002");

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



