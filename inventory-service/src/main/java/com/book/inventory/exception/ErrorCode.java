package com.book.inventory.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // General errors
    INVALID_FIELD(10001, HttpStatus.BAD_REQUEST, "Invalid field"),
    OPTIMISTIC_LOCK(10002, HttpStatus.CONFLICT, "OptimisticLock occurred"),

    // Category-related errors
    CATEGORY_NAME_ALREADY_EXISTS(10100, HttpStatus.CONFLICT, "Category name '{}' already exists"),
    CATEGORY_NOT_FOUND(10101, HttpStatus.NOT_FOUND, "Category with ID '{}' not found"),

    // Product-related errors
    PRODUCT_NAME_ALREADY_EXISTS(10200, HttpStatus.CONFLICT, "Product name '{}' already exists"),
    PRODUCT_ISBN_EXISTED(10201, HttpStatus.CONFLICT, "Product ISBN '{}' already exists"),
    PRODUCT_NOT_FOUND(10202, HttpStatus.NOT_FOUND, "Product with ID '{}' not found"),
    PRODUCT_ALREADY_IN_CART(10203, HttpStatus.CONFLICT, "Product with ID '{}' is already in cart"),
    PRODUCT_OUT_OF_STOCK(10204, HttpStatus.NOT_FOUND, "Product with ID '{}' has been sold out"),
    DUPLICATE_PRODUCT_ID_IN_ORDER(10205, HttpStatus.BAD_REQUEST, "Duplicate productId in order details"),
    INVALID_PRODUCT_ID(10206, HttpStatus.BAD_REQUEST, "Invalid productId(s): {}"),

    // User-related errors
    EMAIL_ALREADY_EXISTS(10300, HttpStatus.CONFLICT, "Email '{}' already exists"),
    PHONE_NUMBER_ALREADY_EXISTS(10301, HttpStatus.CONFLICT, "Phone number '{}' already exists"),
    USERNAME_NOT_FOUND(10302, HttpStatus.NOT_FOUND, "Username '{}' not found"),
    USER_NOT_FOUND(10303, HttpStatus.NOT_FOUND, "User with ID '{}' not found"),
    USER_EXISTED(10304, HttpStatus.BAD_REQUEST, "User already exists"),

    // Authentication and authorization errors
    PASSWORD_INCORRECT(10400, HttpStatus.UNAUTHORIZED, "Password incorrect"),
    CONFIRM_PASSWORD_NOT_MATCH(10401, HttpStatus.BAD_REQUEST, "Confirm password does not match"),
    CURRENT_PASSWORD_INCORRECT(10402, HttpStatus.BAD_REQUEST, "Current password incorrect"),
    UNAUTHORIZED(10403, HttpStatus.FORBIDDEN, "You do not have permission"),
    UNAUTHENTICATED(10404, HttpStatus.UNAUTHORIZED, "Unauthenticated"),
    ACCESS_DENIED(10405, HttpStatus.FORBIDDEN, "Access denied"),

    // Review-related errors
    REVIEW_NOT_FOUND(10500, HttpStatus.NOT_FOUND, "Review with ID '{}' not found"),
    REVIEW_FORBIDDEN(10501, HttpStatus.FORBIDDEN, "You are not allowed to edit this review"),

    // Role-related errors
    ROLE_NOT_FOUND(10600, HttpStatus.NOT_FOUND, "Role with name '{}' not found"),

    // Database-related errors
    DATABASE_CONNECTION_FAILED(10700, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to connect to the database"),
    DATA_INTEGRITY_VIOLATION(10701, HttpStatus.CONFLICT, "Data integrity violation"),
    RECORD_ALREADY_EXISTS(10702, HttpStatus.CONFLICT, "Record already exists"),
    RECORD_NOT_FOUND(10703, HttpStatus.NOT_FOUND, "Record not found"),

    // File-related errors
    FILE_UPLOAD_FAILED(10800, HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed"),
    FILE_NOT_FOUND(10801, HttpStatus.NOT_FOUND, "File not found"),
    UNSUPPORTED_FILE_TYPE(10802, HttpStatus.BAD_REQUEST, "Unsupported file type"),
    FILE_TOO_LARGE(10803, HttpStatus.BAD_REQUEST, "File size exceeds the maximum limit"),

    // Request-related errors
    MALFORMED_JSON_REQUEST(10900, HttpStatus.BAD_REQUEST, "Malformed JSON request"),
    MISSING_REQUIRED_HEADER(10901, HttpStatus.BAD_REQUEST, "Missing required header '{}"),
    UNSUPPORTED_MEDIA_TYPE(10902, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"),
    RATE_LIMIT_EXCEEDED(10903, HttpStatus.TOO_MANY_REQUESTS, "Rate limit exceeded"),

    // Service-related errors
    EXTERNAL_SERVICE_UNAVAILABLE(11000, HttpStatus.SERVICE_UNAVAILABLE, "External service is unavailable"),
    EXTERNAL_SERVICE_TIMEOUT(11001, HttpStatus.GATEWAY_TIMEOUT, "External service timeout"),
    EXTERNAL_SERVICE_ERROR(11002, HttpStatus.BAD_GATEWAY, "Error occurred in external service"),

    // Book-related errors
    BOOK_NOT_FOUND(11100, HttpStatus.NOT_FOUND, "Book with ID '{}' not found"),
    BOOK_ALREADY_EXISTS(11101, HttpStatus.CONFLICT, "Book with title '{}' already exists"),
    BOOK_OUT_OF_STOCK(11102, HttpStatus.BAD_REQUEST, "Book with ID '{}' is out of stock"),
    INVALID_BOOK_CATEGORY(11103, HttpStatus.BAD_REQUEST, "Invalid book category '{}'"),
    BOOK_PRICE_INVALID(11104, HttpStatus.BAD_REQUEST, "Book price must be greater than zero"),

    // Order-related errors
    ORDER_NOT_FOUND(11200, HttpStatus.NOT_FOUND, "Order with ID '{}' not found"),
    ORDER_ALREADY_COMPLETED(11201, HttpStatus.CONFLICT, "Order with ID '{}' is already completed"),
    ORDER_CANCELLATION_NOT_ALLOWED(11202, HttpStatus.FORBIDDEN, "Order with ID '{}' cannot be canceled"),
    INVALID_ORDER_STATUS(11203, HttpStatus.BAD_REQUEST, "Invalid order status '{}'"),
    PAYMENT_FAILED(11204, HttpStatus.PAYMENT_REQUIRED, "Payment for order with ID '{}' failed"),
    PURCHASE_ORDER_NOT_FOUND(11205, HttpStatus.NOT_FOUND, "Purchase order with ID '{}' not found"),

    // Customer-related errors
    CUSTOMER_NOT_FOUND(11300, HttpStatus.NOT_FOUND, "Customer with ID '{}' not found"),
    CUSTOMER_EMAIL_ALREADY_EXISTS(11301, HttpStatus.CONFLICT, "Customer email '{}' already exists"),
    CUSTOMER_PHONE_ALREADY_EXISTS(11302, HttpStatus.CONFLICT, "Customer phone '{}' already exists"),
    INVALID_CUSTOMER_ADDRESS(11303, HttpStatus.BAD_REQUEST, "Invalid customer address '{}'"),

    // Cookie & Security-related errors
    COOKIE_NOT_FOUND(12000, HttpStatus.UNAUTHORIZED, "Cookie '{}' not found"),
    COOKIE_INVALID(12001, HttpStatus.UNAUTHORIZED, "Cookie '{}' is invalid"),
    COOKIE_EXPIRED(12002, HttpStatus.UNAUTHORIZED, "Cookie '{}' has expired"),
    COOKIE_ACCESS_DENIED(12003, HttpStatus.FORBIDDEN, "Access to cookie '{}' denied"),

    // Token-related errors
    TOKEN_NOT_FOUND(12100, HttpStatus.UNAUTHORIZED, "Token not found"),
    TOKEN_INVALID(12101, HttpStatus.UNAUTHORIZED, "Token is invalid"),
    TOKEN_EXPIRED(12102, HttpStatus.UNAUTHORIZED, "Token has expired"),
    TOKEN_SIGNATURE_INVALID(12103, HttpStatus.UNAUTHORIZED, "Token signature is invalid"),
    TOKEN_ALREADY_INVALIDATED(12104, HttpStatus.UNAUTHORIZED, "Token has already been invalidated"),
    TOKEN_TYPE_MISMATCH(12105, HttpStatus.BAD_REQUEST, "Token type mismatch"),
    TOKEN_MISSING_CLAIM(12106, HttpStatus.BAD_REQUEST, "Token missing required claim '{}'"),
    TOKEN_BLACKLISTED(12107, HttpStatus.UNAUTHORIZED, "Token is blacklisted"),

    // JWT-specific errors
    JWT_PARSE_ERROR(12200, HttpStatus.BAD_REQUEST, "Failed to parse JWT"),
    JWT_SIGN_ERROR(12201, HttpStatus.INTERNAL_SERVER_ERROR, "Failed to sign JWT"),
    JWT_VERIFY_ERROR(12202, HttpStatus.UNAUTHORIZED, "Failed to verify JWT"),
    JWT_ISSUER_INVALID(12203, HttpStatus.UNAUTHORIZED, "JWT issuer is invalid"),
    JWT_AUDIENCE_INVALID(12204, HttpStatus.UNAUTHORIZED, "JWT audience is invalid"),
    JWT_CLAIM_INVALID(12205, HttpStatus.BAD_REQUEST, "JWT claim '{}' is invalid"),

    // Author-related errors
    AUTHOR_NOT_FOUND(11400, HttpStatus.NOT_FOUND, "Author with ID '{}' not found"),

    RESET_PASSWORD_TOKEN_NOT_EXISTS(12300, HttpStatus.NOT_FOUND, "Reset password token not exists or expired"),
    RESET_PASSWORD_TOKEN_INCORRECT(12301, HttpStatus.UNAUTHORIZED, "Reset password token incorrect"),

    // Supplier-related errors
    SUPPLIER_NOT_FOUND(13000, HttpStatus.NOT_FOUND, "Supplier with ID '{}' not found"),
    SUPPLIER_NAME_ALREADY_EXISTS(13001, HttpStatus.CONFLICT, "Supplier name '{}' already exists"),
    SUPPLIER_PHONE_ALREADY_EXISTS(13002, HttpStatus.CONFLICT, "Supplier phone '{}' already exists"),
    SUPPLIER_EMAIL_ALREADY_EXISTS(13003, HttpStatus.CONFLICT, "Supplier email '{}' already exists"),

    // Warehouse-related errors
    WAREHOUSE_NOT_FOUND(14000, HttpStatus.NOT_FOUND, "Warehouse with ID '{}' not found"),
    WAREHOUSE_NAME_ALREADY_EXISTS(14001, HttpStatus.CONFLICT, "Warehouse name '{}' already exists");

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
