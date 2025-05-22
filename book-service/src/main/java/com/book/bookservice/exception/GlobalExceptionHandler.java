package com.book.bookservice.exception;

import com.book.bookservice.dto.ErrorResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.data.mapping.PropertyReferenceException;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        private ResponseEntity<ErrorResponse> buildErrorResponse(String code, HttpStatus status, String message,
                        List<ErrorResponse.FieldError> errors) {
                ErrorResponse response = ErrorResponse.of(code, status, message, errors);
                return ResponseEntity.status(status).body(response);
        }

        // Xử lý lỗi khi xảy ra xung đột do khóa lạc quan
        @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
        public ResponseEntity<ErrorResponse> handleOptimisticLockException(ObjectOptimisticLockingFailureException ex) {
                log.error("Optimistic Lock Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                String.valueOf(ErrorCode.OPTIMISTIC_LOCK.getCode()),
                                ErrorCode.OPTIMISTIC_LOCK.getStatus(),
                                ErrorCode.OPTIMISTIC_LOCK.formatMessage("Inventory update", ex.getMessage()),
                                null);
        }

        // Xử lý lỗi tùy chỉnh do ứng dụng định nghĩa
        @ExceptionHandler(CustomException.class)
        public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) {
                log.error("Custom Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                String.valueOf(ex.getErrorCode().getCode()),
                                ex.getStatus(),
                                ex.getErrorMessage(),
                                null);
        }

        // Xử lý lỗi khi tham số đầu vào không hợp lệ
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
                log.error("Validation Exception: {}", ex.getMessage(), ex);
                List<ErrorResponse.FieldError> errors = ex.getBindingResult().getFieldErrors()
                                .stream()
                                .map(fieldError -> ErrorResponse.FieldError.builder()
                                                .field(fieldError.getField())
                                                .message(fieldError.getDefaultMessage())
                                                .build())
                                .toList();
                return buildErrorResponse(
                                "400",
                                HttpStatus.BAD_REQUEST,
                                "Validation failed",
                                errors);
        }

        // Xử lý lỗi khi thiếu tham số bắt buộc trong request
        @ExceptionHandler(MissingServletRequestParameterException.class)
        public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
                log.error("Missing Parameter Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "400",
                                HttpStatus.BAD_REQUEST,
                                "Missing required parameter: " + ex.getParameterName(),
                                null);
        }

        // Xử lý lỗi khi kiểu dữ liệu của tham số không khớp
        @ExceptionHandler(MethodArgumentTypeMismatchException.class)
        public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
                log.error("Type Mismatch Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "400",
                                HttpStatus.BAD_REQUEST,
                                "Invalid parameter: " + ex.getName() + " should be of type "
                                                + Objects.requireNonNull(ex.getRequiredType()).getSimpleName(),
                                null);
        }

        // Xử lý lỗi khi không thể đọc được nội dung JSON trong request
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
                log.error("JSON Parse Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "400",
                                HttpStatus.BAD_REQUEST,
                                "Invalid request body: " + ex.getMostSpecificCause().getMessage(),
                                null);
        }

        // Xử lý lỗi khi tham số không hợp lệ
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
                log.error("Illegal Argument Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "400",
                                HttpStatus.BAD_REQUEST,
                                "Invalid argument: " + ex.getMessage(),
                                null);
        }

        // Xử lý lỗi khi người dùng không có quyền truy cập
        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
                log.error("Access Denied Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "403",
                                HttpStatus.FORBIDDEN,
                                "Access denied: " + ex.getMessage(),
                                null);
        }

        // Xử lý lỗi khi không tìm thấy handler cho URL được yêu cầu
        @ExceptionHandler(NoHandlerFoundException.class)
        public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException ex) {
                log.error("No Handler Found Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "404",
                                HttpStatus.NOT_FOUND,
                                "No handler found for the requested URL",
                                null);
        }

        // Xử lý lỗi khi vi phạm ràng buộc dữ liệu
        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
                log.error("Constraint Violation Exception: {}", ex.getMessage(), ex);
                List<ErrorResponse.FieldError> errors = ex.getConstraintViolations()
                                .stream()
                                .map(violation -> ErrorResponse.FieldError.builder()
                                                .field(violation.getPropertyPath().toString())
                                                .message(violation.getMessage())
                                                .build())
                                .toList();
                return buildErrorResponse(
                                "400",
                                HttpStatus.BAD_REQUEST,
                                "Validation error",
                                errors);
        }

        // Xử lý lỗi khi tham số không thể được bind vào đối tượng
        @ExceptionHandler(BindException.class)
        public ResponseEntity<ErrorResponse> handleBindException(BindException ex) {
                log.error("Bind Exception: {}", ex.getMessage(), ex);
                List<ErrorResponse.FieldError> errors = ex.getBindingResult().getFieldErrors()
                                .stream()
                                .map(fieldError -> ErrorResponse.FieldError.builder()
                                                .field(fieldError.getField())
                                                .message(fieldError.getDefaultMessage())
                                                .build())
                                .toList();
                return buildErrorResponse(
                                "400",
                                HttpStatus.BAD_REQUEST,
                                "Invalid request parameters",
                                errors);
        }

        // Xử lý lỗi khi phương thức HTTP không được hỗ trợ
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleMethodNotSupportedException(
                        HttpRequestMethodNotSupportedException ex) {
                log.error("HTTP Method Not Supported Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "405",
                                HttpStatus.METHOD_NOT_ALLOWED,
                                "HTTP method not supported: " + ex.getMethod(),
                                null);
        }

        // Xử lý lỗi khi gọi Feign client gặp lỗi tùy chỉnh
        @ExceptionHandler(FeignException.class)
        public ResponseEntity<ErrorResponse> handleCustomFeignException(FeignException ex) {
                log.error("Feign Exception: {}", ex.getMessage(), ex);
                String code = String.valueOf(ex.status());
                HttpStatus status = HttpStatus.valueOf(ex.status());
                String message = ex.getMessage();

                // Cố gắng parse nội dung JSON trả về từ Feign để lấy thông tin chi tiết
                try {
                        String content = ex.contentUTF8();
                        if (content != null && content.startsWith("{")) {
                                ObjectMapper mapper = new ObjectMapper();
                                JsonNode node = mapper.readTree(content);
                                code = node.has("errorCode") ? node.get("errorCode").asText() : code;
                                message = node.has("message") ? node.get("message").asText() : message;
                                if (node.has("status")) {
                                        try {
                                                status = HttpStatus.valueOf(node.get("status").asText());
                                        } catch (Exception ignored) {
                                        }
                                }
                        }
                } catch (Exception e) {
                        log.warn("Could not parse Feign error response body: {}", e.getMessage());
                }

                return buildErrorResponse(
                                code,
                                status,
                                message,
                                null);
        }

        // Xử lý lỗi PropertyReferenceException (ví dụ: truy vấn sort/filter sai tên
        // thuộc tính)
        @ExceptionHandler(PropertyReferenceException.class)
        public ResponseEntity<ErrorResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
                log.error("Property Reference Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "400",
                                HttpStatus.BAD_REQUEST,
                                "Invalid property reference: " + ex.getPropertyName(),
                                null);
        }

        // Xử lý lỗi chung cho các lỗi không được xử lý khác
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
                log.error("Unhandled Exception: {}", ex.getMessage(), ex);
                return buildErrorResponse(
                                "500",
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred. Please contact support if the issue persists.",
                                null);
        }
}
