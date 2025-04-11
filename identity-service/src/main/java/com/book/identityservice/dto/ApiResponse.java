package com.book.identityservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private OffsetDateTime timestamp;
    private String message;
    private T data;

    public ApiResponse(String message) {
        this.timestamp = OffsetDateTime.now(ZoneOffset.UTC);
        this.message = message;
    }

    public ApiResponse(T data) {
        this.timestamp = OffsetDateTime.now(ZoneOffset.UTC); // Lấy thời gian hiện tại theo UTC
        this.data = data;
        this.message = "Success";
    }

    public ApiResponse(String message, T data) {
        this.timestamp = OffsetDateTime.now(ZoneOffset.UTC); // Lấy thời gian hiện tại theo UTC
        this.data = data;
        this.message = message;
    }

}
