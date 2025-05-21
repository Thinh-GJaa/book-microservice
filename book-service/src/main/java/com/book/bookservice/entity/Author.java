package com.book.bookservice.entity;

import com.book.bookservice.enums.Gender;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String authorId;

    @Column(nullable = false, unique = true)
    String authorName;

    @Enumerated(EnumType.STRING)
    Gender gender;

    // Thêm thuộc tính cho tác giả
    LocalDate birthDate;

    String nationality;

    @Column(length = 2000)
    String description;

    @ManyToMany(mappedBy = "authors")
    Set<Product> products = new HashSet<>();

}
