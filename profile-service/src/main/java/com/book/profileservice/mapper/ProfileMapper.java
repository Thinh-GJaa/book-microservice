package com.book.profileservice.mapper;


import com.book.profileservice.dto.request.UpdateProfileRequest;
import com.book.profileservice.dto.response.MyProfileResponse;
import com.book.profileservice.dto.response.UpdateProfileResponse;
import org.mapstruct.Mapper;
import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.response.CreatedProfileResponse;
import com.book.profileservice.entity.UserProfile;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProfileMapper {

    UserProfile toUserProfile(ProfileCreationRequest request);

    CreatedProfileResponse toCreatedProfileResponse(UserProfile userProfile);

    MyProfileResponse toMyProfileResponse(UserProfile userProfile);

    UserProfile updateUserProfileFromRequest(UpdateProfileRequest updateProfileRequest, @MappingTarget UserProfile userProfile);

    UpdateProfileResponse toUpdateProfileResponse (UserProfile userProfile);


}

