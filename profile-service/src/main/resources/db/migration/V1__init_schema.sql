-- V1__init_schema.sql: Tạo schema cho profile-service

CREATE TABLE user_profile (
    user_id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    gender VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(255) NOT NULL,
    dob DATE NOT NULL
);
