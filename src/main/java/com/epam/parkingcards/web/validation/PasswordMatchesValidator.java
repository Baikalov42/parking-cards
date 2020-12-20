package com.epam.parkingcards.web.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.epam.parkingcards.web.request.UserRegistrationRequest;
import com.epam.parkingcards.web.validation.annotation.PasswordMatches;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final UserRegistrationRequest user = (UserRegistrationRequest) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }
}