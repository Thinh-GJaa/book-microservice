package com.book.profileservice.exception;

import com.book.profileservice.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLockException(ObjectOptimisticLockingFailureException ex) {
        ErrorResponse errorResponse = ErrorResponse.of(
                String.valueOf(ErrorCode.OPTIMISTIC_LOCK.getCode()),
                ErrorCode.OPTIMISTIC_LOCK.getStatus(),
                ErrorCode.OPTIMISTIC_LOCK.formatMessage("Inventory update", ex.getMessage()),
                null
        );
        return new ResponseEntity<>(errorResponse, ErrorCode.OPTIMISTIC_LOCK.getStatus());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
        ErrorResponse response = ErrorResponse.of(
                String.valueOf(ex.getErrorCode().getCode()),
                ex.getStatus(),
                ex.getErrorMessage(),
                null
        );
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse.FieldError> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> ErrorResponse.FieldError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .build())
                .toList();
        ErrorResponse response = ErrorResponse.of(
                String.valueOf(ex.getBody().getStatus()),
                HttpStatus.valueOf(ex.getStatusCode().value()),
                "Validation failed",
                errors
        );
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        ErrorResponse response = ErrorResponse.of(
                "400",
                HttpStatus.BAD_REQUEST,
                "Missing required parameter: " + ex.getParameterName(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ErrorResponse response = ErrorResponse.of(
                "400",
                HttpStatus.BAD_REQUEST,
                "Invalid parameter: " + ex.getName() + " should be of type " + Objects.requireNonNull(ex.getRequiredType()).getSimpleName(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
        ErrorResponse response = ErrorResponse.of(
                "400",
                HttpStatus.BAD_REQUEST,
                "Invalid request body: " + ex.getMostSpecificCause().getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
