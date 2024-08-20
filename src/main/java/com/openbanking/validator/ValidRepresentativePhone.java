package com.openbanking.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RepresentativePhoneValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRepresentativePhone {

    String message() default "Invalid representative phone number";

    int min() default 9;

    int max() default 10;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}