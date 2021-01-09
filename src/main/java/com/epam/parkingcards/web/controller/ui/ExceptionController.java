package com.epam.parkingcards.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice(annotations = Controller.class)
public class ExceptionController {

    @ExceptionHandler(Throwable.class)
    public String handleException(Model model, Throwable ex) {

        model.addAttribute("ex", ex.getMessage());
        return "error-message-page";
    }
}
