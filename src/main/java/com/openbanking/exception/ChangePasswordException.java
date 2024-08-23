package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class ChangePasswordException extends CustomException{
    public ChangePasswordException(String message) {
        super(CommonErrorCodes.CHANGE_PASSWORD_ERROR, message);
    }
}
