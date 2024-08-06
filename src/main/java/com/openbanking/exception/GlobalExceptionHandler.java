package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;
import com.openbanking.comon.ErrorCode;
import com.openbanking.comon.ErrorResponse;
import com.openbanking.comon.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseBuilder<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errRs = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        return new ResponseBuilder<>(errorCode.getHttpStatus().value(), "Failure", errRs);
    }

    @ExceptionHandler(Exception.class)
    public ResponseBuilder<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Handling general exception: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.INTERNAL_SERVER_ERROR.getCode(), CommonErrorCodes.INTERNAL_SERVER_ERROR.getMessage());
        return new ResponseBuilder<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failure", errRs);
    }
}
