package com.book.identityservice.service;

import com.book.identityservice.dto.request.ChangePasswordRequest;
import com.book.identityservice.dto.request.LoginRequest;
import com.book.identityservice.dto.request.IntrospectRequest;
import com.book.identityservice.dto.request.LogoutRequest;
import com.book.identityservice.dto.response.LoginResponse;
import com.book.identityservice.dto.response.IntrospectResponse;
import com.book.identityservice.dto.response.RefreshResponse;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;

public interface AuthenticationService {

    IntrospectResponse introspect(IntrospectRequest request);

    LoginResponse login(LoginRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse httpServletResponse) throws ParseException, JOSEException;

    RefreshResponse refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ParseException, JOSEException;


    void changePassword(ChangePasswordRequest changePasswordRequest);

}
