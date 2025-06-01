-- V1__init_schema.sql: Tạo đầy đủ schema cho identity-service

CREATE TABLE users (
    user_id VARCHAR(36) PRIMARY KEY,
    password VARCHAR(255),
    email VARCHAR(255) COLLATE utf8mb4_unicode_ci UNIQUE,
    email_verified BOOLEAN DEFAULT FALSE NOT NULL,
    role VARCHAR(255) COLLATE utf8mb4_unicode_ci
);

CREATE TABLE roles (
    role_name VARCHAR(255) PRIMARY KEY,
    description VARCHAR(255)
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

CREATE TABLE invalidated_token (
    id VARCHAR(36) PRIMARY KEY,
    expiry_time DATETIME
);
