package com.book.identityservice.controller;

import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.ChangePasswordRequest;
import com.book.identityservice.dto.request.ResetPasswordRequest;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.ProfileResponse;
import com.book.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "User", description = "APIs for user registration, password management, and profile operations.")
public class UserController {

    UserService userService;

    @Operation(summary = "Create user", description = "Register a new user account. Returns profile information.")
    @PostMapping("/users")
    ResponseEntity<ApiResponse<?>> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<ProfileResponse> response = ApiResponse.<ProfileResponse>builder()
                .message("Create profile successfully")
                .data(userService.createUser(request))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Change password", description = "Change the password for the authenticated user.")
    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(
            @Parameter(description = "Change password request body", required = true) @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Change password successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Forgot password", description = "Send a password reset link to the user's email address.")
    @GetMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("A password reset link has been sent to your email.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Verify reset password link", description = "Verify the validity of a password reset link.")
    @GetMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> verifyResetPasswordLink(
            @Parameter(description = "User's email address", required = true) @RequestParam String email,
            @Parameter(description = "Password reset token", required = true) @RequestParam String token) {

        userService.verifyResetPasswordLink(email, token);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Password reset link verified")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Reset password", description = "Reset the user's password using a valid token.")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        userService.resetPassword(resetPasswordRequest);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Reset password successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

}
