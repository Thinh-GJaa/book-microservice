package com.book.profileservice.controller;

import com.book.profileservice.dto.ApiResponse;
import com.book.profileservice.dto.request.UpdateProfileRequest;
import com.book.profileservice.dto.response.ProfileResponse;
import com.book.profileservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {

    UserProfileService userProfileService;

    @GetMapping("/my-profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMyProfile() {
        ApiResponse<ProfileResponse> response = ApiResponse.<ProfileResponse>builder()
                .data(userProfileService.getMyProfile())
                .message("Get my profile successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/my-profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        ApiResponse<ProfileResponse> response = ApiResponse.<ProfileResponse>builder()
                .data(userProfileService.updateMyProfile(updateProfileRequest))
                .message("Update my profile successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileByUserId(
            @PathVariable String userId){
        ApiResponse<ProfileResponse> apiResponse = ApiResponse.<ProfileResponse>builder()
                .message("Get profile by Id successfully")
                .data(userProfileService.getProfileByUserId(userId))
                .build();

        return ResponseEntity.ok(apiResponse);
    }




}
