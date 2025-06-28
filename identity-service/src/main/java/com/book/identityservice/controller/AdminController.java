package com.book.identityservice.controller;

import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.AdminCreationRequest;
import com.book.identityservice.dto.response.ProfileResponse;
import com.book.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Tag(name = "Admin", description = "APIs for admin account management.")
public class AdminController {
    UserService userService;

    @Operation(summary = "Create admin", description = "Register a new admin account. Returns profile information.")
    @PostMapping("/admin")
    ResponseEntity<ApiResponse<ProfileResponse>> createUser(
            @Parameter(description = "Admin creation request body", required = true) @RequestBody @Valid AdminCreationRequest request) {
        ApiResponse<ProfileResponse> response = ApiResponse.<ProfileResponse>builder()
                .message("Create profile successfully")
                .data(userService.createAdmin(request))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
