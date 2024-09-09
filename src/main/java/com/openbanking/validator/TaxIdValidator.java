package com.openbanking.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaxIdValidator implements ConstraintValidator<ValidTaxId, String> {
    private static final String TAX_ID_PATTERN = "^\\d{10}-\\d{3}$";

    @Override
    public void initialize(ValidTaxId constraintAnnotation) {
    }

    @Override
    public boolean isValid(String taxId, ConstraintValidatorContext context) {
        return taxId != null && taxId.matches(TAX_ID_PATTERN);
    }
}