package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/ui/cars")
public class CarUiController {

    @Autowired
    private CarService carService;

    @SecuredForAdmin
    @GetMapping("/page/{pageNumber}")
    public String getAll(@Valid @PathVariable int pageNumber, Model model) {
        List<CarEntity> all = carService.findAll(pageNumber);
        model.addAttribute("cars", all);
        return "admin/cars-list";
    }

    @SecuredForAdmin
    @GetMapping("/update-page/{id}")
    public String toUpdatePage(@PathVariable long id, Model model) {

        CarEntity carEntity = carService.findById(id);
        model.addAttribute("car", carEntity);
        return "admin/cars-update";
    }

    @SecuredForAdmin
    @PutMapping
    public String update(CarEntity carEntity, Model model) {

        CarEntity updated = carService.update(carEntity);
        model.addAttribute("updated", updated);
        return "success-message-page";
    }

    @SecuredForAdmin
    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id, Model model) {

        carService.deleteById(id);
        return "success-message-page";
    }

}
