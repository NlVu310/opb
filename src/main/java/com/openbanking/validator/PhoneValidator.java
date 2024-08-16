package com.openbanking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {
    private int min;
    private int max;

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return false;
        }
        return phoneNumber.matches("^0\\d{" + (min - 1) + "," + (max - 1) + "}$");
    }
}
