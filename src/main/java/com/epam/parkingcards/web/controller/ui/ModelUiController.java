package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/ui/models")
public class ModelUiController {

    @Autowired
    private ModelService modelService;

    @SecuredForAdmin
    @GetMapping("/page/{pageNumber}")
    public String getAll(@Valid @PathVariable int pageNumber, Model model) {
        List<ModelEntity> all = modelService.findAllActive(pageNumber);
        model.addAttribute("models", all);
        return "admin/models-list";
    }
}
