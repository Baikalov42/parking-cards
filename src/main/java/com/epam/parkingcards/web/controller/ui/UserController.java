package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.UserService;
import com.epam.parkingcards.web.mapper.UserMapper;
import com.epam.parkingcards.web.request.UserRegistrationRequest;
import com.epam.parkingcards.web.request.UserUpdateRequest;
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
@RequestMapping("/ui/users")
public class UserController {

    public static final String MESSAGE_VIEW = "success-message-page";

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper mapper;

    @GetMapping("/create-page")
    public String toCreatePage(Model model) {
        model.addAttribute("userCreateRequest", new UserRegistrationRequest());
        return "admin/users/user-create";
    }

    @PostMapping
    public String create(@Valid UserRegistrationRequest request, Model model) {
        long id = userService.register(mapper.toUser(request));
        model.addAttribute("message", "ok, new id = " + id);
        return MESSAGE_VIEW;
    }

    @SecuredForAdmin
    @GetMapping("/page/{pageNumber}")
    public String getAll(@PathVariable int pageNumber, Model model) {
        pageNumber--;

        Page<UserEntity> all = userService.findAll(pageNumber);
        model.addAttribute("users", all);
        int totalPages = all.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/users/users-list";
    }

    @SecuredForAdmin
    @GetMapping("/update-page/{id}")
    public String toUpdatePage(@PathVariable long id, Model model) {

        UserEntity userEntity = userService.findById(id);

        model.addAttribute("userEntity", userEntity);
        model.addAttribute("userUpdateRequest", new UserUpdateRequest());

        return "admin/users/user-edit";
    }

    @SecuredForAdmin
    @PostMapping("/edit")
    public String update(@Valid UserUpdateRequest request, Model model) {

        UserEntity update = userService.update(mapper.toUser(request));
        model.addAttribute("message", "ok");
        return MESSAGE_VIEW;
    }

    @SecuredForAdmin
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id, Model model) {

        userService.deleteById(id);
        model.addAttribute("message", "ok");

        return MESSAGE_VIEW;
    }
}
