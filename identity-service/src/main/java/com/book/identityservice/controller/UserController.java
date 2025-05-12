package com.book.identityservice.controller;


import com.book.identityservice.dto.ApiResponse;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.CreatedProfileResponse;
import com.book.identityservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {
    UserService userService;

    @PostMapping("/users")
    ApiResponse<?> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<CreatedProfileResponse>builder()
                .message("success")
                .data(userService.createUser(request))
                .build();
    }

    @PutMapping("/test")
    ApiResponse<?> test(){
        log.info("test");
        return new ApiResponse<>("test");
    }




}
