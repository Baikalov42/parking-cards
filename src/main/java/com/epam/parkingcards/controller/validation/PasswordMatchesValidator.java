package com.epam.parkingcards.controller.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.epam.parkingcards.controller.request.UserRegistrationRequest;
import com.epam.parkingcards.controller.validation.annotation.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final UserRegistrationRequest user = (UserRegistrationRequest) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}