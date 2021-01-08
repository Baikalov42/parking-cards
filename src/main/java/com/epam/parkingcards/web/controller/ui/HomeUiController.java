package com.epam.parkingcards.web.controller.ui;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeUiController {

    @GetMapping("ui/index")
    public String getAll() {
        return "index";
    }
}
