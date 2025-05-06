package com.book.profileservice.entity;

import com.book.profileservice.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class UserProfile {

    @Id
    String userId;

    @Column(name = "email", unique = true)
    String email;

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name", nullable = false)
    String lastName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    Gender gender;

    @Column(name = "phone_number", nullable = false, unique = true)
    String phoneNumber;

    @Column(name = "address", nullable = false)
    String address;

    @Column(name = "dob", nullable = false)
    LocalDate dob;

}