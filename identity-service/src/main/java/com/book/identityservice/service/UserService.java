package com.book.identityservice.service;


import com.book.identityservice.dto.event.UpdateEmailEvent;
import com.book.identityservice.dto.request.AdminCreationRequest;
import com.book.identityservice.dto.request.ChangePasswordRequest;
import com.book.identityservice.dto.request.ResetPasswordRequets;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.ProfileResponse;

public interface UserService {
    ProfileResponse createUser(UserCreationRequest request);

    ProfileResponse createAdmin(AdminCreationRequest adminCreationRequest);

    void updateEmailEvent(UpdateEmailEvent event);

    void changePassword(ChangePasswordRequest changePasswordRequest);

    String forgotPassword(String email);

    void resetPassword(ResetPasswordRequets resetPasswordRequets);

    void verifyResetPasswordLink(String email, String token);

}
