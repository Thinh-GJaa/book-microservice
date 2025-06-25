package com.book.profileservice.controller;


import com.book.profileservice.dto.ApiResponse;
import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.response.ProfileResponse;
import com.book.profileservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {

    UserProfileService userProfileService;

    @PostMapping("/users")
    public ApiResponse<?> createProfileUser(@Valid @RequestBody ProfileCreationRequest request) {
        return ApiResponse.<ProfileResponse>builder()
                .message("Created profile user successfully")
                .data(userProfileService.createProfile(request))
                .build();
    }

    @GetMapping("/users/email/{userId}")
    public ApiResponse<?> getEmailByUserId(@PathVariable String userId) {
        return ApiResponse.<String>builder()
                .message("Get email by userID successfully")
                .data(userProfileService.getEmailByUserId(userId))
                .build();
    }
}
