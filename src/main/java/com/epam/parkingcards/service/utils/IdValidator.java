package com.epam.parkingcards.service.utils;

import com.epam.parkingcards.exception.ValidationException;

public final class IdValidator {

    private IdValidator() {
        throw new UnsupportedOperationException("Not possible create IdValidator object");
    }

    public static void validate(long... id) {
        for (long current : id) {
            if (current < 1) {
                throw new ValidationException(
                        String.format("Id must be greater than 0, input id = %d", current));
            }
        }
    }

    public static void validate(long id) {
        if (id < 1) {
            throw new ValidationException(
                    String.format("Id must be greater than 0, input id = %d", id));
        }
    }
}