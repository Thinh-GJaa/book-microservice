package com.book.profileservice.service;

import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.request.UpdateProfileRequest;
import com.book.profileservice.dto.response.ProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserProfileService {
    ProfileResponse createProfile(ProfileCreationRequest request);

    ProfileResponse getMyProfile();

    ProfileResponse updateMyProfile(UpdateProfileRequest updateProfileRequest);

    ProfileResponse getProfileByUserId(String userId);

    Page<ProfileResponse> getProfiles(String keyword, Pageable pageable);


}
