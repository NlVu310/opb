package com.openbanking.exception.base_exception;

import com.openbanking.comon.CommonErrorCodes;
import com.openbanking.comon.ErrorCode;
import com.openbanking.comon.ErrorResponse;
import com.openbanking.comon.ResponseBuilder;
import com.openbanking.exception.authen_exception.AuthenException;
import com.openbanking.exception.delete_exception.DeleteException;
import com.openbanking.exception.insert_exception.InsertException;
import com.openbanking.exception.resource_not_found_exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
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
        return new ResponseBuilder<>(HttpStatus.BAD_REQUEST.value(), "Validation error", errors.toString());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseBuilder<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
        log.error("Insert entity err: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.INSERT_ERROR.getCode(), ex.getMessage());
        return new ResponseBuilder<>(HttpStatus.NOT_FOUND.value(), "Insert err", errRs);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseBuilder<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("Validation error: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.VALIDATION_ERROR.getCode(), ex.getMessage());
        return new ResponseBuilder<>(HttpStatus.BAD_REQUEST.value(), "Validation error", errRs);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseBuilder<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("Entity query error: {}", ex.getMessage());
        var errRs = new ErrorResponse(CommonErrorCodes.UNAUTHORIZED_ACCESS.getCode(), ex.getMessage());
        return new ResponseBuilder<>(HttpStatus.UNAUTHORIZED.value(), "Entity query error", errRs);
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
        var errRs = new ErrorResponse(CommonErrorCodes.INTERNAL_SERVER_ERROR.getCode(), ex.getMessage());
        return new ResponseBuilder<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "System err", errRs);
    }

    @ExceptionHandler(DeleteException.class)
    public ResponseBuilder<ErrorResponse> handleDeleteException(DeleteException ex) {
        log.error("Delete error: {}", ex.getMessage());
        var errRs = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseBuilder<>(ex.getStatus().value(), "Delete error", errRs);
    }

    @ExceptionHandler(InsertException.class)
    public ResponseBuilder<ErrorResponse> handleInsertException(InsertException ex) {
        log.error("Insert error: {}", ex.getMessage());
        var errRs = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseBuilder<>(ex.getStatus().value(), "Insert error", errRs);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseBuilder<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource error: {}", ex.getMessage());
        var errRs = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseBuilder<>(ex.getStatus().value(), "Resource Not Found error", errRs);
    }

    @ExceptionHandler(AuthenException.class)
    public ResponseBuilder<ErrorResponse> handleAuthException(AuthenException ex) {
        log.error("Auth error: {}", ex.getMessage());
        var errRs = new ErrorResponse(ex.getCode(), ex.getMessage());
        return new ResponseBuilder<>(ex.getStatus().value(), "Auth error", errRs);
    }
}

