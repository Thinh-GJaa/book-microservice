package com.book.profileservice.service;

import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.request.UpdateProfileRequest;
import com.book.profileservice.dto.response.CreatedProfileResponse;
import com.book.profileservice.dto.response.MyProfileResponse;
import com.book.profileservice.dto.response.UpdateProfileResponse;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;

public interface UserProfileService {
    CreatedProfileResponse createProfile(ProfileCreationRequest request);

    MyProfileResponse getMyProfile();

    UpdateProfileResponse updateMyProfile(UpdateProfileRequest updateProfileRequest);
}
