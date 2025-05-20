package com.book.profileservice.repository;

import com.book.profileservice.entity.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    boolean existsByUserId(String userId);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Page<UserProfile> findByLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String lastName, String email, Pageable pageable);
}
