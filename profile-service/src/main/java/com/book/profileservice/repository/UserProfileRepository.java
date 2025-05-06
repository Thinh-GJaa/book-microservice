package com.book.profileservice.repository;

import com.book.profileservice.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

}
