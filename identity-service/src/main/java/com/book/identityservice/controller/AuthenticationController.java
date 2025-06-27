package com.book.identityservice.controller;

import java.text.ParseException;

import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.*;
import com.book.identityservice.dto.response.LoginResponse;
import com.book.identityservice.dto.response.IntrospectResponse;
import com.book.identityservice.dto.response.RefreshResponse;
import com.book.identityservice.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "APIs for authentication, token management, and session operations.")
public class AuthenticationController {

        AuthenticationService authenticationService;

        @Operation(summary = "Login", description = "Authenticate user and return access/refresh tokens. Requires credentials.")
        @PostMapping("/token")
        ResponseEntity<ApiResponse<LoginResponse>> login(
                        @Parameter(description = "Login request body", required = true) @Valid @RequestBody LoginRequest loginRequest,
                        HttpServletResponse httpServletResponse) {
                var data = authenticationService.login(loginRequest, httpServletResponse);
                ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                                .message("Login successfully")
                                .data(data).build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Introspect token", description = "Validate and get information about an access token. Requires authentication.")
        @PostMapping("/introspect")
        ResponseEntity<ApiResponse<IntrospectResponse>> introspect(
                        @Parameter(description = "Introspect request body", required = true) @Valid @RequestBody IntrospectRequest request) {
                var result = authenticationService.introspect(request);
                ApiResponse<IntrospectResponse> response = ApiResponse.<IntrospectResponse>builder()
                                .data(result)
                                .build();
                return ResponseEntity.ok(response);
        }

        @Operation(summary = "Refresh token", description = "Refresh access token using a valid refresh token. Requires authentication.")
        @PostMapping("/refresh")
        ResponseEntity<ApiResponse<RefreshResponse>> refreshToken(HttpServletRequest request,
                        HttpServletResponse response)
                        throws ParseException, JOSEException {
                RefreshResponse result = authenticationService.refreshToken(request, response);
                ApiResponse<RefreshResponse> apiResponse = ApiResponse.<RefreshResponse>builder()
                                .message("Refresh token successfully")
                                .data(result)
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

        @Operation(summary = "Logout", description = "Logout user and invalidate tokens. Requires authentication.")
        @PostMapping("/logout")
        ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response)
                        throws ParseException, JOSEException {
                authenticationService.logout(request, response);
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                                .message("Logout successfully")
                                .build();
                return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
        }

}