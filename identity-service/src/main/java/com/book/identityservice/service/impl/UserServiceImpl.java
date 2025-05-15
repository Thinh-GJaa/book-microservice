package com.book.identityservice.service.impl;

import com.book.identityservice.dto.event.NotificationEvent;
import com.book.identityservice.dto.event.UpdateEmailEvent;
import com.book.identityservice.dto.request.AdminCreationRequest;
import com.book.identityservice.dto.request.ProfileCreationRequest;
import com.book.identityservice.dto.request.UserCreationRequest;
import com.book.identityservice.dto.response.CreatedProfileResponse;
import com.book.identityservice.entity.User;
import com.book.identityservice.exception.CustomException;
import com.book.identityservice.exception.ErrorCode;
import com.book.identityservice.mapper.ProfileMapper;
import com.book.identityservice.mapper.UserMapper;
import com.book.identityservice.producer.NotificationProducer;
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
    NotificationProducer notificationProducer;
    ProfileClient profileClient;

    @Override
    @Transactional
    public CreatedProfileResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByEmail(request.getEmail()))
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, request.getEmail());


        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setEmailVerified(false);

        user = userRepository.save(user);

        ProfileCreationRequest profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest.setUserId(user.getUserId());

        var createdProfileResponse = profileClient.createProfile(profileRequest).getData();

        notificationProducer.createProfileNotification(request.getLastName(), request.getEmail());

        return createdProfileResponse;
    }

    @Override
    @Transactional
    public CreatedProfileResponse createAdmin(AdminCreationRequest adminCreationRequest) {
        if (userRepository.existsByEmail(adminCreationRequest.getEmail()))
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, adminCreationRequest.getEmail());


        User user = userMapper.toUser(adminCreationRequest);
        user.setPassword(passwordEncoder.encode(adminCreationRequest.getPassword()));
        user.setRole("ADMIN");
        user.setEmailVerified(false);

        user = userRepository.save(user);

        ProfileCreationRequest profileRequest = profileMapper.toProfileCreationRequest(adminCreationRequest);
        profileRequest.setUserId(user.getUserId());

        var createdProfileResponse = profileClient.createProfile(profileRequest).getData();

        notificationProducer.createProfileNotification(adminCreationRequest.getLastName()
                , adminCreationRequest.getEmail());

        return createdProfileResponse;
    }

    @Override
    public void updateEmailEvent(UpdateEmailEvent event) {

        User user = userRepository.findById(event.getUserId())
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND, event.getUserId()));

        //Nếu thay đổi trùng với email hiện tại thì return
        if(event.getEmail().equals(user.getEmail()))
            return;


        if(userRepository.existsByEmail(event.getEmail())
            && !event.getEmail().equals(user.getEmail())){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, event.getEmail());
        }

        user.setEmail(event.getEmail());

        userRepository.save(user);

    }

}
