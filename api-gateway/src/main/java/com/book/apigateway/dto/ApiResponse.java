package com.book.apigateway.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    OffsetDateTime timestamp;
    String message;
    T data;

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
