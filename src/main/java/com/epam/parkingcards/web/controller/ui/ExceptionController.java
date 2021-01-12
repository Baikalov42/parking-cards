package com.epam.parkingcards.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(annotations = Controller.class)
public class ExceptionController {

    @ExceptionHandler(Throwable.class)
    public String handleOthers(Model model, Throwable ex) {

        model.addAttribute("message", ex.getMessage());
        return "error-message-page";
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(Model model, BindException ex) {
        StringBuilder message = new StringBuilder();

        for (ObjectError error : ex.getAllErrors()) {
            message.append(error.getDefaultMessage())
                    .append(" ");
        }
        model.addAttribute("message", message.toString());
        return "error-message-page";
    }
}
