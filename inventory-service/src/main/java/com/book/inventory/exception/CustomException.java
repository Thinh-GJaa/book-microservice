package com.book.inventory.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // Bỏ qua các field null khi serialize JSON
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final HttpStatus status;
    private final String errorMessage;

    public CustomException(ErrorCode errorCode, Object... args) {
        super(errorCode.formatMessage(args));
        this.errorCode = errorCode;
        this.status = errorCode.getStatus();
        this.errorMessage = errorCode.formatMessage(args);
    }
}
