package com.book.identityservice.repository;

import java.util.Optional;

import com.book.identityservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByEmail(String email);

//    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByEmail(String email);
}