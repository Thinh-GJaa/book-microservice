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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

        AuthenticationService authenticationService;

        @PostMapping("/token")
        ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
                        HttpServletResponse httpServletResponse) {
                var data = authenticationService.login(loginRequest, httpServletResponse);
                ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                                .message("Login successfully")
                                .data(data).build();
                return ResponseEntity.ok(response);
        }

        @PostMapping("/introspect")
        ResponseEntity<ApiResponse<IntrospectResponse>> introspect(@Valid @RequestBody IntrospectRequest request) {
                var result = authenticationService.introspect(request);
                ApiResponse<IntrospectResponse> response = ApiResponse.<IntrospectResponse>builder()
                                .data(result)
                                .build();
                return ResponseEntity.ok(response);
        }

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

        @PostMapping("/logout")
        ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response)
                        throws ParseException, JOSEException {
                authenticationService.logout(request, response);
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                                .message("Logout successfully")
                                .build();
                return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
        }

        @PutMapping("/change-password")
        public ResponseEntity<ApiResponse<?>> changePassword(
                        @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
                authenticationService.changePassword(changePasswordRequest);
                ApiResponse<Void> apiResponse = ApiResponse.<Void>builder().message("Change password successfully")
                                .build();
                return ResponseEntity.ok(apiResponse);
        }

}