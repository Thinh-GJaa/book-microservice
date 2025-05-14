package com.book.identityservice.service.impl;

import com.book.event.NotificationEvent;
import com.book.identityservice.dto.request.ProfileCreationRequest;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.CreatedProfileResponse;
import com.book.identityservice.entity.User;
import com.book.identityservice.exception.CustomException;
import com.book.identityservice.exception.ErrorCode;
import com.book.identityservice.mapper.ProfileMapper;
import com.book.identityservice.mapper.UserMapper;
import com.book.identityservice.repository.UserRepository;
import com.book.identityservice.repository.httpclient.ProfileClient;
import com.book.identityservice.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    KafkaTemplate<String, Object> kafkaTemplate;
    ProfileClient profileClient;

    @Override
    @Transactional
    public CreatedProfileResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByEmail(request.getEmail()))
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, request.getEmail());

        // if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
        // throw new CustomException(ErrorCode.PHONE_NUMBER_ALREADY_EXISTS,
        // request.getPhoneNumber());

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setEmailVerified(false);

        user = userRepository.save(user);

        ProfileCreationRequest profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getUserId());

        var createdProfileResponse = profileClient.createProfile(profileRequest).getData();

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .channel("Email")
                .recipient(request.getEmail())
                .subject("Welcome to microservice (ThinhGJaa)")
                .body("Hello, " + request.getLastName())
                .build();


        kafkaTemplate.send("create-profile-topic", notificationEvent);
        return createdProfileResponse;
    }

}
