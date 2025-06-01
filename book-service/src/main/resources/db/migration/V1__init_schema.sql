-- V1__init_full_schema.sql: Tạo đầy đủ schema cho book-service

CREATE TABLE products (
    product_id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    publisher VARCHAR(255),
    publish_year INT,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    price DECIMAL(19,2) NOT NULL,
    language VARCHAR(50),
    create_date DATETIME,
    update_date DATETIME,
    cover_image_id VARCHAR(36)
);

CREATE TABLE authors (
    author_id VARCHAR(36) PRIMARY KEY,
    author_name VARCHAR(255) NOT NULL,
    gender VARCHAR(20),
    birth_date DATE,
    nationality VARCHAR(100),
    description VARCHAR(2000)
);

CREATE TABLE categories (
    category_id VARCHAR(36) PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000)
);

CREATE TABLE images (
    image_id VARCHAR(36) PRIMARY KEY,
    image_url VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE outbox_event (
    id VARCHAR(36) PRIMARY KEY,
    aggregate_type VARCHAR(100),
    aggregate_id VARCHAR(100),
    event_type VARCHAR(100),
    payload TEXT,
    created_at DATETIME,
    status VARCHAR(20)
);

CREATE TABLE product_author (
    product_id VARCHAR(36) NOT NULL,
    author_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (product_id, author_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (author_id) REFERENCES authors(author_id)
);

CREATE TABLE product_category (
    product_id VARCHAR(36) NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id),
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);

ALTER TABLE products ADD CONSTRAINT fk_cover_image FOREIGN KEY (cover_image_id) REFERENCES images(image_id);
