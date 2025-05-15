package com.book.identityservice.service;


import com.book.identityservice.dto.event.UpdateEmailEvent;
import com.book.identityservice.dto.request.AdminCreationRequest;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.CreatedProfileResponse;

public interface UserService {
    CreatedProfileResponse createUser(UserCreationRequest request);

    CreatedProfileResponse createAdmin(AdminCreationRequest adminCreationRequest);

    void updateEmailEvent(UpdateEmailEvent event);
}
