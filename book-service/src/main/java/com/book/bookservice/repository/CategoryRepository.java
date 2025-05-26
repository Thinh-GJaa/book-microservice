package com.book.bookservice.repository;

import com.book.bookservice.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Category findByCategoryName(String categoryName);

    Page<Category> findByCategoryNameContainingIgnoreCase(String keyword, Pageable pageable);
}
