//package com.book.identityservice.service;
//
//import java.util.HashSet;
//import java.util.List;
//
//import com.book.identityservice.constant.PredefinedRole;
//import com.book.identityservice.dto.request.UserCreationRequest;
//import com.book.identityservice.dto.response.UserResponse;
//import com.book.identityservice.entity.Role;
//import com.book.identityservice.entity.User;
//import com.book.identityservice.exception.CustomException;
//import com.book.identityservice.exception.ErrorCode;
//import com.book.identityservice.mapper.ProfileMapper;
//import com.book.identityservice.mapper.UserMapper;
//import com.book.identityservice.repository.RoleRepository;
//import com.book.identityservice.repository.UserRepository;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
//public class UserService {
//    UserRepository userRepository;
//    RoleRepository roleRepository;
//    UserMapper userMapper;
//    ProfileMapper profileMapper;
//    PasswordEncoder passwordEncoder;
////    ProfileClient profileClient;
////    KafkaTemplate<String, Object> kafkaTemplate;
//
//    public UserResponse createUser(UserCreationRequest request) {
//        User user = userMapper.toUser(request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        HashSet<Role> roles = new HashSet<>();
//
//        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);
//
//        user.setRoles(roles);
//        user.setEmailVerified(false);
//
//        try {
//            user = userRepository.save(user);
//        } catch (DataIntegrityViolationException exception) {
//            throw new CustomException(ErrorCode.USER_EXISTED);
//        }
//
//        var profileRequest = profileMapper.toProfileCreationRequest(request);
//        profileRequest.setUserId(user.getId());
//
////        var profile = profileClient.createProfile(profileRequest);
////
////        NotificationEvent notificationEvent = NotificationEvent.builder()
////                .channel("EMAIL")
////                .recipient(request.getEmail())
////                .subject("Welcome to bookteria")
////                .body("Hello, " + request.getUsername())
////                .build();
////
////        // Publish message to kafka
////        kafkaTemplate.send("notification-delivery", notificationEvent);
////
////        var userCreationReponse = userMapper.toUserResponse(user);
////        userCreationReponse.setId(profile.getResult().getId());
////
////        return userCreationReponse;
////    }
//
//    public UserResponse getMyInfo() {
//        var context = SecurityContextHolder.getContext();
//        String name = context.getAuthentication().getName();
//
//        User user = userRepository.findByUsername(name).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTED));
//
//        return userMapper.toUserResponse(user);
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    public UserResponse updateUser(String userId, UserUpdateRequest request) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
//
//        userMapper.updateUser(user, request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        var roles = roleRepository.findAllById(request.getRoles());
//        user.setRoles(new HashSet<>(roles));
//
//        return userMapper.toUserResponse(userRepository.save(user));
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    public void deleteUser(String userId) {
//        userRepository.deleteById(userId);
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<UserResponse> getUsers() {
//        log.info("In method get Users");
//        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    public UserResponse getUser(String id) {
//        return userMapper.toUserResponse(
//                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
//    }
//}
