package com.openbanking.exception.authen_exception;

import com.openbanking.comon.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ChangePasswordExceptionEnum implements ErrorCode {
    AUTH_PASS_DEF_ERROR(HttpStatus.UNAUTHORIZED, "New password must be different from default password", "AUTH_PASS_001"),
    AUTH_PASS_NEW_ERROR(HttpStatus.UNAUTHORIZED, "New password is not the same as re-enter new password", "AUTH_PASS_002");
    private final HttpStatus status;
    @Getter
    private final String message;
    @Getter
    private final String code;

    ChangePasswordExceptionEnum(HttpStatus status, String message, String code) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

}



