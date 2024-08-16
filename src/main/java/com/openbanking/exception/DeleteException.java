package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class DeleteException extends CustomException {
    public DeleteException(String message) {
        super(CommonErrorCodes.DELETE_ERROR, message);
    }
}
