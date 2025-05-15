package com.book.identityservice.controller;

import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.AdminCreationRequest;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.CreatedProfileResponse;
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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminController {
    UserService userService;

    @PostMapping("/admin")
    ResponseEntity<ApiResponse<CreatedProfileResponse>> createUser(@RequestBody @Valid AdminCreationRequest request) {
        ApiResponse<CreatedProfileResponse> response = ApiResponse.<CreatedProfileResponse>builder()
                .message("Create profile successfully")
                .data(userService.createAdmin(request))
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
