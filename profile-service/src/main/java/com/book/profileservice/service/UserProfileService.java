package com.book.profileservice.service;

import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.response.CreatedProfileResponse;

public interface UserProfileService {
    CreatedProfileResponse createProfile(ProfileCreationRequest request);

}
