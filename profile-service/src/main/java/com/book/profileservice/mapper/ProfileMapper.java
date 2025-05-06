package com.book.profileservice.mapper;


import org.mapstruct.Mapper;
import com.book.profileservice.dto.request.ProfileCreationRequest;
import com.book.profileservice.dto.response.CreatedProfileResponse;
import com.book.profileservice.entity.UserProfile;


@Mapper(componentModel = "spring")
public interface ProfileMapper {

    UserProfile toUserProfile(ProfileCreationRequest request);

    CreatedProfileResponse toCreatedProfileResponse(UserProfile userProfile);
}
