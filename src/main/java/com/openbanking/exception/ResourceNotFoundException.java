package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;

public class ResourceNotFoundException extends CustomException {
    public ResourceNotFoundException(String message) {
        super(CommonErrorCodes.RESOURCE_NOT_FOUND);
    }
}
