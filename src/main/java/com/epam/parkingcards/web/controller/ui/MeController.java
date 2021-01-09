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
import com.epam.parkingcards.web.request.UserUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @GetMapping("/update-page")
    public String toUpdatePage(Principal principal, Model model) {
        UserEntity me = userService.findByEmail(principal.getName());

        model.addAttribute("me", me);
        model.addAttribute("userUpdateRequest", new UserUpdateRequest());

        return "user/user-edit";
    }

    @PostMapping("/edit")
    public String update(@Valid UserUpdateRequest request, Model model) {

        UserEntity update = userService.update(userMapper.toUser(request));
        model.addAttribute("message", "ok");
        return MESSAGE_VIEW;
    }

    @GetMapping("/cars")
    public String getAllCars(Principal principal, Model model) {
        UserEntity me = userService.findByEmail(principal.getName());
        List<CarEntity> all = carService.findByUserId(me.getId());
        model.addAttribute("cars", all);
        return "user/user-cars";
    }

    @GetMapping("/create-car")
    public String toCreatePage(Principal principal, Model model) {
        UserEntity me = userService.findByEmail(principal.getName());

        Map<Long, String> usersMap = new HashMap<>();
        usersMap.put(me.getId(),
                String.format("%s %s", me.getFirstName(), me.getLastName()));

        model.addAttribute("modelUsersMap", usersMap);
        model.addAttribute("carCreateRequest", new CarCreateRequest());
        model.addAttribute("modelModelsMap", modelService.getModelsMap());
        model.addAttribute("modelBrandsMap", brandService.getBrandsMap());

        return "user/user-create-car";
    }

    @PostMapping("/car/edit")
    public String create(@Valid CarCreateRequest request, Model model) {
        long id = carService.create(carMapper.toCar(request));
        model.addAttribute("message", "ok, new id = " + id);
        return MESSAGE_VIEW;
    }

}
