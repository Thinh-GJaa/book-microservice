package com.book.profileservice.service.impl;

import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.response.CreatedProfileResponse;
import com.book.profileservice.entity.UserProfile;
import com.book.profileservice.exception.CustomException;
import com.book.profileservice.exception.ErrorCode;
import com.book.profileservice.mapper.ProfileMapper;
import com.book.profileservice.repository.UserProfileRepository;
import com.book.profileservice.service.UserProfileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
}
