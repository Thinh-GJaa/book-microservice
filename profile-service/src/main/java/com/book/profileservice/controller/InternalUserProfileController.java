package com.book.profileservice.controller;


import com.book.profileservice.dto.ApiResponse;
import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.response.CreatedProfileResponse;
import com.book.profileservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InternalUserProfileController {

    UserProfileService userProfileService;

    @PostMapping("/users")
    public ApiResponse<?> createProfileUser(@Valid @RequestBody ProfileCreationRequest request) {
        return ApiResponse.<CreatedProfileResponse>builder()
                .message("Created profile user successfully")
                .data(userProfileService.createProfile(request))
                .build();
    }
}
