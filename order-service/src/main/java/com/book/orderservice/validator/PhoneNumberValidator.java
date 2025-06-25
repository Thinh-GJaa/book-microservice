package com.book.orderservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {



    private static final String VIETNAM_PHONE_REGEX = "^(0[3|5|7|8|9])+([0-9]{8})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty())
            return false;
        return value.matches(VIETNAM_PHONE_REGEX);
    }
}