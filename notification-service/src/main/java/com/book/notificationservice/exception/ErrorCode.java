package com.book.notificationservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    ID_REQUIRE(10000, HttpStatus.BAD_REQUEST, "'{}Id' is required"),
    CATEGORY_NAME_ALREADY_EXISTS(10001, HttpStatus.CONFLICT, "Category name '{}' already exists"),
    CATEGORY_NOT_FOUND(10002, HttpStatus.NOT_FOUND, "Category with ID '{}' not found"),
    PRODUCT_NAME_ALREADY_EXISTS(10003, HttpStatus.CONFLICT, "Product name '{}' already exists"),
    PRODUCT_NOT_FOUND(10004, HttpStatus.NOT_FOUND, "Product with ID '{}' not found"),
    EMAIL_ALREADY_EXISTS(10005, HttpStatus.CONFLICT, "Email '{}' already exists"),
    PHONE_NUMBER_ALREADY_EXISTS(10006, HttpStatus.CONFLICT, "Phone number '{}' already exists"),
    USERNAME_NOT_FOUND(10007, HttpStatus.NOT_FOUND, "Username '{}' not found"),
    PASSWORD_INCORRECT(10008, HttpStatus.UNAUTHORIZED, "Password incorrect"),
    CONFIRM_PASSWORD_NOT_MATCH(10009, HttpStatus.BAD_REQUEST, "Confirm password does not match"),
    CURRENT_PASSWORD_INCORRECT(10010, HttpStatus.BAD_REQUEST, "Current password incorrect"),
    REVIEW_NOT_FOUND(10011, HttpStatus.NOT_FOUND, "Review with ID '{}' not found"),
    REVIEW_FORBIDDEN(10012, HttpStatus.FORBIDDEN, "You are not allowed to edit this review"),
    UNAUTHORIZED(10013, HttpStatus.FORBIDDEN, "You do not have permission"),
    UNAUTHENTICATED(10014, HttpStatus.UNAUTHORIZED, "Unauthenticated"),
    USER_EXISTED(10015, HttpStatus.BAD_REQUEST, "User existed"),


    ROLE_NOT_FOUND(10011, HttpStatus.NOT_FOUND, "Role with name '{}' not found"),
    USER_NOT_FOUND(10012, HttpStatus.NOT_FOUND, "User with ID '{}' not found"),
    PRODUCT_ALREADY_IN_CART(10001, HttpStatus.CONFLICT, "Product with ID {} is already in cart"),

    PRODUCT_OUT_OF_STOCK(10002, HttpStatus.NOT_FOUND, "Product with ID {} has been sold out"),
    INVALID_FIELD(10005, HttpStatus.BAD_REQUEST, "Invalid field"),
    OPTIMISTIC_LOCK(10006, HttpStatus.CONFLICT, "OptimisticLock occurred");

    private final int code;
    private final HttpStatus status;
    private final String messageTemplate;

    ErrorCode(int code, HttpStatus status, String messageTemplate) {
        this.code = code;
        this.status = status;
        this.messageTemplate = messageTemplate;
    }

    public String formatMessage(Object... args) {
        return String.format(messageTemplate.replace("{}", "%s"), args);
    }
}

