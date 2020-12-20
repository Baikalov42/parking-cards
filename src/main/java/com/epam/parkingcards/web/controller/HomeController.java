package com.epam.parkingcards.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/index")
    public String getWelcomePage() {
        return "Some welcome text";
    }
}
