package com.book.identityservice.controller;


import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.CreatedProfileResponse;
import com.book.identityservice.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<CreatedProfileResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<CreatedProfileResponse>builder()
                .data(userService.createUser(request))
                .build();
    }


}
