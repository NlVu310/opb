package com.openbanking.exception;

import com.openbanking.comon.CommonErrorCodes;
import com.openbanking.comon.ErrorCode;
import com.openbanking.comon.ErrorResponse;
import com.openbanking.comon.ResponseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseBuilder<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseBuilder<>(HttpStatus.BAD_REQUEST.value(), "Validation Error", errors.toString());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseBuilder<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.RESOURCE_NOT_FOUND.getCode(), CommonErrorCodes.RESOURCE_NOT_FOUND.getMessage());
        return new ResponseBuilder<>(HttpStatus.NOT_FOUND.value(), "Failure", errRs);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseBuilder<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("Validation error: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.VALIDATION_ERROR.getCode(), CommonErrorCodes.VALIDATION_ERROR.getMessage());
        return new ResponseBuilder<>(HttpStatus.BAD_REQUEST.value(), "Failure", errRs);
    }

    @ExceptionHandler(AuthenticateException.class)
    public ResponseBuilder<ErrorResponse> handleAuthenticateException(AuthenticateException ex) {
        log.error("Authentication error: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.UNAUTHORIZED_ACCESS.getCode(), ex.getMessage());
        return new ResponseBuilder<>(HttpStatus.UNAUTHORIZED.value(), "Failure", errRs);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseBuilder<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse errRs = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        return new ResponseBuilder<>(errorCode.getHttpStatus().value(), "Failure", errRs);
    }

    @ExceptionHandler(Exception.class)
    public ResponseBuilder<ErrorResponse> handleGeneralException(Exception ex) {
        ex.printStackTrace();
        log.error("Handling general exception: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.INTERNAL_SERVER_ERROR.getCode(), CommonErrorCodes.INTERNAL_SERVER_ERROR.getMessage());
        return new ResponseBuilder<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failure", errRs);
    }
}

