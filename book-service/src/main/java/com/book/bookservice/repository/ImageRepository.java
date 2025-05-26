package com.book.bookservice.repository;

import com.book.bookservice.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    // ...custom query methods if needed...
}
