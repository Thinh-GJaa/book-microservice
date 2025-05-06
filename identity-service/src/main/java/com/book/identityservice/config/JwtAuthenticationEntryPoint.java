package com.book.identityservice.config;

import java.io.IOException;
import java.time.OffsetDateTime;

import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.ErrorResponse;
import com.book.identityservice.exception.CustomException;
import com.book.identityservice.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //Xử lí các yêu cầu không có token hoặc không hợp lệ
    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.of(
                String.valueOf(errorCode.getCode()),
                errorCode.getStatus(),
                errorCode.getMessageTemplate(),
                null
        );

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}