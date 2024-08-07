package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class AuthenticateException extends CustomException{
    public AuthenticateException() {
        super(CommonErrorCodes.UNAUTHORIZED_ACCESS);
    }
}
