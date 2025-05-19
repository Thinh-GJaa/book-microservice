package com.book.identityservice.mapper;

import com.book.identityservice.dto.request.AdminCreationRequest;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.ProfileResponse;
import com.book.identityservice.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    User toUser(AdminCreationRequest adminCreationRequest);

    ProfileResponse toProfileResponse(User user);

}
