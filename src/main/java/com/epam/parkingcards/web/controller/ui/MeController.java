package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.BrandService;
import com.epam.parkingcards.service.CarService;
import com.epam.parkingcards.service.ModelService;
import com.epam.parkingcards.service.UserService;
import com.epam.parkingcards.web.mapper.CarMapper;
import com.epam.parkingcards.web.mapper.UserMapper;
import com.epam.parkingcards.web.request.CarCreateRequest;
import com.epam.parkingcards.web.request.CarUpdateRequest;
import com.epam.parkingcards.web.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/ui/me")
public class MeController {

    public static final String MESSAGE_VIEW = "success-message-page";
    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelService modelService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarMapper carMapper;

    @GetMapping
    public String getMyCard(Principal principal, Model model) {
        UserEntity me = userService.findByEmail(principal.getName());
        model.addAttribute("me", me);
        return "user/user-card";
    }

    @GetMapping("/cars")
    public String getMyCars(Principal principal, Model model) {
        UserEntity me = userService.findByEmail(principal.getName());
        List<CarEntity> all = carService.findByUserId(me.getId());
        model.addAttribute("cars", all);
        return "user/user-cars";
    }

    @GetMapping("/update-page")
    public String toUserUpdatePage(Principal principal, Model model) {
        UserEntity me = userService.findByEmail(principal.getName());
        model.addAttribute("me", me);
        model.addAttribute("userUpdateRequest", new UserUpdateRequest());
        return "user/user-edit";
    }

    @PostMapping("/edit")
    public String updateUser(@Valid UserUpdateRequest request, Model model) {
        UserEntity update = userService.update(userMapper.toUser(request));
        model.addAttribute("message", "ok");
        return MESSAGE_VIEW;
    }

    @GetMapping("/create-car")
    public String toCarCreatePage(Principal principal, Model model) {
        UserEntity user = userService.findByEmail(principal.getName());

        model.addAttribute("user", user);
        model.addAttribute("carCreateRequest", new CarCreateRequest());
        model.addAttribute("modelModelsMap", modelService.getModelsMap());
        model.addAttribute("modelBrandsMap", brandService.getBrandsMap());

        return "user/user-create-car";
    }

    @PostMapping("/car/create")
    public String createCar(@Valid CarCreateRequest request, Model model) {
        long id = carService.create(carMapper.toCar(request));
        model.addAttribute("message", "ok, new id = " + id);
        return MESSAGE_VIEW;
    }


    @GetMapping("cars/update/{id}")
    public String toUserUpdatePage(@PathVariable long id, Principal principal, Model model) {

        CarEntity carEntity = carService.findById(id);

        model.addAttribute("carEntity", carEntity);
        model.addAttribute("carUpdateRequest", new CarUpdateRequest());
        model.addAttribute("modelModelsMap", modelService.getModelsMap());
        model.addAttribute("modelBrandsMap", brandService.getBrandsMap());

        return "user/cars-edit";
    }

    @PostMapping("cars/edit")
    public String updateCar(@Valid CarUpdateRequest request, Model model) {
        CarEntity updated = carService.update(carMapper.toCar(request));
        model.addAttribute("message", updated);
        return MESSAGE_VIEW;
    }

    @GetMapping("cars/delete/{id}")
    public String deleteCar(@PathVariable long id, Model model) {
        carService.deleteById(id);
        model.addAttribute("message", id);
        return MESSAGE_VIEW;
    }
}
