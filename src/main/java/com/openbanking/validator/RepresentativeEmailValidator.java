package com.openbanking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RepresentativeEmailValidator implements ConstraintValidator<ValidRepresentativeEmail, String> {
    private static final String EMAIL_PATTERN = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }
        return email.matches(EMAIL_PATTERN);
    }
}