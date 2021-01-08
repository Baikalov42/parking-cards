package com.epam.parkingcards.web.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeRestController {

    @GetMapping("/api/index")
    public String getWelcomePage() {
        return "Some welcome text";
    }
}
