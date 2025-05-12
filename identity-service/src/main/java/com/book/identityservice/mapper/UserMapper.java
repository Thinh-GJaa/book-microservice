package com.book.identityservice.mapper;

import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.UserInfoResponse;
import com.book.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserInfoResponse toUserInfoResponse(User user);

}
