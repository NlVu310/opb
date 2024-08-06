package com.openbanking.comon;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String getCode();

    String getMessage();

    HttpStatus getHttpStatus();
}
