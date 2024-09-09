package com.openbanking.exception.base_exception;

import com.openbanking.comon.CommonErrorCodes;
import com.openbanking.exception.base_exception.CustomException;

public class AuthenticateException extends CustomException {

    public AuthenticateException(String message) {
        super(CommonErrorCodes.UNAUTHORIZED_ACCESS, message);
    }
}
