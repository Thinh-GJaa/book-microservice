package com.book.identityservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class) // Liên kết với class validator
@Target({ElementType.FIELD}) // Áp dụng cho field
@Retention(RetentionPolicy.RUNTIME) // Giữ lại ở runtime
public @interface ValidPassword {
    String message() default "Invalid password. It must be at least 8 characters long, include an uppercase letter, a lowercase letter, a digit, and a special character.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {}; // Metadata cho validation
}
