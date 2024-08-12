package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class InsertException extends CustomException{
    public InsertException(String message) {
        super(CommonErrorCodes.INSERT_ERROR, message);
    }
}
