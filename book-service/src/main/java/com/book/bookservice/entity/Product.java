package com.book.bookservice.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(generator = "uuid-generator")
    @GenericGenerator(name = "uuid-generator", strategy = "uuid2")
    String productId;

    @Column(nullable = false)
    String title;

    String publisher;

    @Column(name = "publish_year")
    Integer publishYear;

    @Column(name = "isbn", unique = true, nullable = false)
    String isbn;

    @Column(nullable = false)
    BigDecimal price;

    String language;

    @Column(name = "create_date", updatable = false)
    LocalDateTime createDate;

    @Column(name = "update_date")
    LocalDateTime updateDate;

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_authors", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    Set<Author> authors = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    Image coverImage;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

}
