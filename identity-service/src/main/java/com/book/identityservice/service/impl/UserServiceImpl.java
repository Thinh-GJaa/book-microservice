package com.book.identityservice.service.impl;

import com.book.identityservice.dto.event.UpdateEmailEvent;
import com.book.identityservice.dto.request.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    StringRedisTemplate redisTemplate;

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

        notificationProducer.createProfileNotification(adminCreationRequest.getLastName(),
                adminCreationRequest.getEmail());

        return createdProfileResponse;
    }

    @Override
    public void updateEmailEvent(UpdateEmailEvent event) {

        User user = userRepository.findById(event.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, event.getUserId()));

        // Nếu thay đổi trùng với email hiện tại thì return
        if (event.getEmail().equals(user.getEmail()))
            return;

        if (userRepository.existsByEmail(event.getEmail())
                && !event.getEmail().equals(user.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS, event.getEmail());
        }

        user.setEmail(event.getEmail());

        userRepository.save(user);

    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {

        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USERNAME_NOT_FOUND, userId));

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmNewPassword()))
            throw new CustomException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCH);

        if (!passwordEncoder.matches(changePasswordRequest.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.PASSWORD_INCORRECT);

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);
        log.info("Thay đổi mật khẩu thành công");
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, email));

        String token = UUID.randomUUID().toString();

        // Lưu token vào Redis
        redisTemplate.opsForValue().set("reset-password-token::" + email, token, 5, TimeUnit.MINUTES);

        String resetLink = "http://localhost:8080/api/v1/auth/reset-password?email=" + email + "&token=" + token;

        notificationProducer.resetLinkNotification(email, resetLink);

        return token;
    }

    @Override
    public void resetPassword(ResetPasswordRequets resetPasswordRequest) {

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword()))
            throw new CustomException(ErrorCode.CONFIRM_PASSWORD_NOT_MATCH);

        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, resetPasswordRequest.getEmail()));

        // Kiểm tra token từ Redis
        checkTokenValid(resetPasswordRequest.getEmail(), resetPasswordRequest.getToken());

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

        userRepository.save(user);

        // Xóa token khỏi Redis sau khi reset thành công
        redisTemplate.delete("reset-password-token::" + resetPasswordRequest.getEmail());
    }

    @Override
    public void verifyResetPasswordLink(String email, String token) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, email));

        checkTokenValid(email, token);
    }

    public String getResetPasswordToken(String email) {
        return redisTemplate.opsForValue().get("reset-password-token::" + email);
    }

    private void checkTokenValid(String email, String token) {
        String tokenInCache = getResetPasswordToken(email);

        log.info("Email: {}, Token: {}", email, tokenInCache);

        if (tokenInCache == null)
            throw new CustomException(ErrorCode.RESET_PASSWORD_TOKEN_NOT_EXISTS);

        if (!tokenInCache.equals(token))
            throw new CustomException(ErrorCode.RESET_PASSWORD_TOKEN_INCORRECT);
    }


}
