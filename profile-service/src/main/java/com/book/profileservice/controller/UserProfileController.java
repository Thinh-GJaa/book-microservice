package com.book.profileservice.controller;

import com.book.profileservice.dto.ApiResponse;
import com.book.profileservice.dto.request.UpdateProfileRequest;
import com.book.profileservice.dto.response.MyProfileResponse;
import com.book.profileservice.dto.response.UpdateProfileResponse;
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
    public ResponseEntity<ApiResponse<MyProfileResponse>> getMyProfile() {
        ApiResponse<MyProfileResponse> response = ApiResponse.<MyProfileResponse>builder()
                .data(userProfileService.getMyProfile())
                .message("Get my profile successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/my-profile")
    public ResponseEntity<ApiResponse<UpdateProfileResponse>> updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        ApiResponse<UpdateProfileResponse> response = ApiResponse.<UpdateProfileResponse>builder()
                .data(userProfileService.updateMyProfile(updateProfileRequest))
                .message("Update my profile successfully")
                .build();
        return ResponseEntity.ok(response);
    }
}
