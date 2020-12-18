package com.epam.parkingcards.service.utils;

import com.epam.parkingcards.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class IdValidator {

    public void validate(long... id) {
        for (long current : id) {
            if (current < 1) {
                throw new ValidationException(
                        String.format("Id must be greater than 0, input id = %d", current));
            }
        }
    }

    public void validate(long id) {
        if (id < 1) {
            throw new ValidationException(
                    String.format("Id must be greater than 0, input id = %d", id));
        }
    }
}