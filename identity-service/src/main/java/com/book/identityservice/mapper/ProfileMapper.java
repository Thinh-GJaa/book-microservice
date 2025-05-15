package com.book.identityservice.mapper;

import com.book.identityservice.dto.request.AdminCreationRequest;
import com.book.identityservice.dto.request.ProfileCreationRequest;
import com.book.identityservice.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest creationRequest);

    ProfileCreationRequest toProfileCreationRequest(AdminCreationRequest creationRequest);
}
