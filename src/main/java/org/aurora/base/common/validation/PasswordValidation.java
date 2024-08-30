package org.aurora.base.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidation implements ConstraintValidator<Password, String> {

    private int min;
    private int max;
    private boolean nullable;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.nullable = constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null) return nullable;

        return value.length() >= min && value.length() <= max;
    }
}
