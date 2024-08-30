package org.aurora.base.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class MobileValidation implements ConstraintValidator<Mobile, String> {

    private String regexp;

    @Override
    public void initialize(Mobile constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        Pattern pattern = Pattern.compile(regexp);

        return pattern.matcher(value).matches();
    }
}
