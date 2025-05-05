package com.book.identityservice.service;

import com.book.identityservice.dto.request.AuthenticationRequest;
import com.book.identityservice.dto.request.IntrospectRequest;
import com.book.identityservice.dto.request.LogoutRequest;
import com.book.identityservice.dto.request.RefreshRequest;
import com.book.identityservice.dto.response.AuthenticationResponse;
import com.book.identityservice.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {

    IntrospectResponse introspect(IntrospectRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void logout(LogoutRequest request) throws ParseException, JOSEException;

//    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
}
