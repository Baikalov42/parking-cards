package com.epam.parkingcards.web.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/api/index")
    public String getWelcomePage() {
        return "Some welcome text";
    }
}
