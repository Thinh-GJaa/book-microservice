package com.book.profileservice.service.impl;

import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.request.UpdateProfileRequest;
import com.book.profileservice.dto.response.CreatedProfileResponse;
import com.book.profileservice.dto.response.MyProfileResponse;
import com.book.profileservice.dto.response.UpdateProfileResponse;
import com.book.profileservice.entity.UserProfile;
import com.book.profileservice.exception.CustomException;
import com.book.profileservice.exception.ErrorCode;
import com.book.profileservice.mapper.ProfileMapper;
import com.book.profileservice.repository.UserProfileRepository;
import com.book.profileservice.service.UserProfileService;
import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileServiceImpl implements UserProfileService {

    UserProfileRepository userProfileRepository;

    ProfileMapper profileMapper;

    @Override
    public CreatedProfileResponse createProfile(ProfileCreationRequest request) {

        if (userProfileRepository.existsByUserId(request.getUserId()))
            throw new CustomException(ErrorCode.USER_EXISTED, request.getUserId());

        if (userProfileRepository.existsByEmail(request.getEmail()))
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, request.getEmail());

        if (userProfileRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new CustomException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS, request.getPhoneNumber());

        UserProfile userProfile = profileMapper.toUserProfile(request);

        userProfile = userProfileRepository.save(userProfile);

        return profileMapper.toCreatedProfileResponse(userProfile);
    }

    @Override
    public MyProfileResponse getMyProfile() {

        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USERNAME_NOT_FOUND, userId));

        return profileMapper.toMyProfileResponse(userProfile);
    }

    @Override
    public UpdateProfileResponse updateMyProfile(UpdateProfileRequest updateProfileRequest) {

        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(()-> new CustomException(ErrorCode.USERNAME_NOT_FOUND, userId));

        if(userProfileRepository.existsByEmail(updateProfileRequest.getEmail())
                && !updateProfileRequest.getEmail().equals(userProfile.getEmail()))
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, updateProfileRequest.getEmail());

        if(userProfileRepository.existsByPhoneNumber(updateProfileRequest.getPhoneNumber())
                && !updateProfileRequest.getPhoneNumber().equals(userProfile.getPhoneNumber()))
            throw new CustomException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS, updateProfileRequest.getPhoneNumber());

        userProfile = profileMapper.updateUserProfileFromRequest(updateProfileRequest, userProfile);

        userProfile = userProfileRepository.save(userProfile);

        return profileMapper.toUpdateProfileResponse(userProfile);
    }
}
