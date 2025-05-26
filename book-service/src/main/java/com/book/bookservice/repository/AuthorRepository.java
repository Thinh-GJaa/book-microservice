package com.book.bookservice.repository;


import com.book.bookservice.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    Page<Author> findByAuthorNameContainingIgnoreCaseOrNationalityContainingIgnoreCaseOrDescriptionContainingIgnoreCase
        (String authorName, String nationality, String description, Pageable pageable);
}
