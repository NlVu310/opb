package com.openbanking.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RepresentativePhoneValidator implements ConstraintValidator<ValidRepresentativePhone, String> {

    private int min;
    private int max;

    @Override
    public void initialize(ValidRepresentativePhone constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null) {
            return true;
        }
        String regex = "^0\\d{" + (min - 1) + "," + (max - 1) + "}$";
        return phone.matches(regex);
    }
}

