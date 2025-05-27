package com.book.inventory.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = { PhoneNumberValidator.class })
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number. A valid Vietnamese number must start with 03, 05, 07, 08, or 09 and contain exactly 10 digits.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}