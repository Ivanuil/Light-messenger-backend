package edu.example.light_messenger.validation.validators;

import edu.example.light_messenger.validation.constraints.PasswordComplexityConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordComplexityValidator implements ConstraintValidator<PasswordComplexityConstraint, String> {

    private static final int MIN_LENGTH = 6;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        if (s.length() < MIN_LENGTH) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Password is too short")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}