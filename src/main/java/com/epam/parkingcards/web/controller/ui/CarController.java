package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.BrandService;
import com.epam.parkingcards.service.CarService;
import com.epam.parkingcards.service.ModelService;
import com.epam.parkingcards.service.UserService;
import com.epam.parkingcards.web.mapper.CarMapper;
import com.epam.parkingcards.web.request.CarCreateRequest;
import com.epam.parkingcards.web.request.CarUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ui/cars")
@SecuredForAdmin
public class CarController {

    public static final String MESSAGE_VIEW = "success-message-page";

    @Autowired
    private CarService carService;
    @Autowired
    private CarMapper mapper;
    @Autowired
    private ModelService modelService;
    @Autowired
    private UserService userService;
    @Autowired
    private BrandService brandService;

    @GetMapping("/create-page")
    public String toCreatePage(Model model) {
        model.addAttribute("carCreateRequest", new CarCreateRequest());
        model.addAttribute("modelModelsMap", modelService.getModelsMap());
        model.addAttribute("modelUsersMap", userService.getUsersMap());
        model.addAttribute("modelBrandsMap", brandService.getBrandsMap());

        return "admin/cars/car-create";
    }

    @PostMapping
    public String create(@Valid CarCreateRequest request, Model model) {
        long id = carService.create(mapper.toCar(request));
        model.addAttribute("message", "ok, new id = " + id);
        return MESSAGE_VIEW;
    }

    @GetMapping("/page/{pageNumber}")
    public String getAll(@PathVariable int pageNumber, Model model) {
        pageNumber--;
        Page<CarEntity> all = carService.findAll(pageNumber);
        model.addAttribute("cars", all);
        int totalPages = all.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/cars/cars-list";
    }

    @GetMapping("/by-user/{userId}")
    public String getByUserId(@PathVariable long userId, Model model) {

        List<CarEntity> carsByUser = carService.findByUserId(userId);
        UserEntity user = userService.findById(userId);

        model.addAttribute("cars", carsByUser);
        model.addAttribute("user", user);

        return "admin/cars/cars-by-user";
    }

    @GetMapping("/update-page/{id}")
    public String toUpdatePage(@PathVariable long id, Model model) {

        CarEntity carEntity = carService.findById(id);

        model.addAttribute("carEntity", carEntity);
        model.addAttribute("carUpdateRequest", new CarUpdateRequest());
        model.addAttribute("modelModelsMap", modelService
                .getModelsMap(carEntity.getModelEntity().getBrandEntity().getId()));
        model.addAttribute("modelUsersMap", userService.getUsersMap());
        model.addAttribute("modelBrandsMap", brandService.getBrandsMap());

        return "admin/cars/car-edit";
    }

    @PostMapping("/edit")
    public String update(@Valid CarUpdateRequest request, Model model) {

        CarEntity updated = carService.update(mapper.toCar(request));
        model.addAttribute("message", updated);
        return MESSAGE_VIEW;
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id, Model model) {

        carService.deleteById(id);
        model.addAttribute("message", id);
        return MESSAGE_VIEW;
    }

}
