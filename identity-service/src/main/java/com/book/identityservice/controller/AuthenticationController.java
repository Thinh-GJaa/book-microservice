package com.book.identityservice.controller;

import java.text.ParseException;

import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.AuthenticationRequest;
import com.book.identityservice.dto.request.IntrospectRequest;
import com.book.identityservice.dto.request.LogoutRequest;
import com.book.identityservice.dto.request.RefreshRequest;
import com.book.identityservice.dto.response.AuthenticationResponse;
import com.book.identityservice.dto.response.IntrospectResponse;
import com.book.identityservice.service.AuthenticationService;
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
    ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        log.info("Thinh: " + request.toString());
        var data = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().data(data).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@Valid @RequestBody IntrospectRequest request) {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().data(result).build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .message("success")
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@Valid @RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}