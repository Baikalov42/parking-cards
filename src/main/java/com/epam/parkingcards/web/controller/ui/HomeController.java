package com.epam.parkingcards.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("ui/index")
    public String getAll() {
        return "index";
    }
}
