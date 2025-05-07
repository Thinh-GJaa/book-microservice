package com.book.identityservice.controller;

import java.text.ParseException;

import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.LoginRequest;
import com.book.identityservice.dto.request.IntrospectRequest;
import com.book.identityservice.dto.request.LogoutRequest;
import com.book.identityservice.dto.request.RefreshRequest;
import com.book.identityservice.dto.response.LoginResponse;
import com.book.identityservice.dto.response.IntrospectResponse;
import com.book.identityservice.dto.response.RefreshResponse;
import com.book.identityservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        var data = authenticationService.login(loginRequest, httpServletResponse);
        return ApiResponse.<LoginResponse>builder()
                .message("Login successfully")
                .data(data).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request) {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response)
            throws ParseException, JOSEException {
        RefreshResponse result = authenticationService.refreshToken(request, response);
        return ApiResponse.<RefreshResponse>builder()
                .message("Refresh token successfully")
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request, HttpServletResponse httpServletResponse) throws ParseException, JOSEException {
        authenticationService.logout(request, httpServletResponse);
        return ApiResponse.<Void>builder()
                .message("Logout successfully")
                .build();
    }
}