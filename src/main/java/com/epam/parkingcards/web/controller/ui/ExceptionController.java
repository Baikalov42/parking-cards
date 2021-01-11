package com.epam.parkingcards.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(annotations = Controller.class)
public class ExceptionController {

    @ExceptionHandler(Throwable.class)
    public String handleAllException(Model model, Throwable ex) {

        model.addAttribute("message", ex.getMessage());
        return "error-message-page";
    }

    @ExceptionHandler(BindException.class)
    public String handleValidationException(Model model, BindException ex) {

        StringBuilder message = new StringBuilder();

        for (FieldError fieldError : ex.getFieldErrors()) {
            message.append("[")
                    .append(fieldError.getField())
                    .append(":")
                    .append(fieldError.getDefaultMessage())
                    .append("]")
                    .append("\n");
        }
        System.err.println(message.toString());
        model.addAttribute("message", message.toString());
        return "error-message-page";
    }
}
