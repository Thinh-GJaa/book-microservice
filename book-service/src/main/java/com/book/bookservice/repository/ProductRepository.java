package com.book.bookservice.repository;


import com.book.bookservice.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    boolean existsByIsbn(String isbn);
    Page<Product> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Product> findAllByCategory_CategoryId(String categoryId, Pageable pageable);
    Page<Product> findAllByAuthors_AuthorId(String authorId, Pageable pageable);

    Set<Product> findAllByProductIdIn(Set<String> productIds);
}
