package com.book.identityservice.controller;

import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.ChangePasswordRequest;
import com.book.identityservice.dto.request.ResetPasswordRequets;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.CreatedProfileResponse;
import com.book.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    UserService userService;

    @PostMapping("/users")
    ResponseEntity<ApiResponse<?>> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<CreatedProfileResponse> response = ApiResponse.<CreatedProfileResponse>builder()
                .message("Create profile successfully")
                .data(userService.createUser(request))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse<?>> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Change password successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email){
        userService.forgotPassword(email);
        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("A password reset link has been sent to your email.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> verifyResetPasswordLink(
            @RequestParam String email,
            @RequestParam String token){

        userService.verifyResetPasswordLink(email, token);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Password reset link verified")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequets resetPasswordRequets){

        userService.resetPassword(resetPasswordRequets);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .message("Reset password successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }







}
