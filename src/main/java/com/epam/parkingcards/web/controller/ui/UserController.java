package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/ui/users")
public class UserController {

    @Autowired
    private UserService userService;

    @SecuredForAdmin
    @GetMapping("/page/{pageNumber}")
    public String getAll(@Valid @PathVariable int pageNumber, Model model) {
        List<UserEntity> all = userService.findAll(pageNumber);
        model.addAttribute("users", all);
        return "admin/users/users-list";
    }

    @SecuredForAdmin
    @GetMapping("/update-page/{id}")
    public String toUpdatePage(@PathVariable long id, Model model) {

        UserEntity userEntity = userService.findById(id);
        model.addAttribute("user", userEntity);
        return "admin/users/user-edit";
    }

    @SecuredForAdmin
    @PostMapping("/edit")
    public String update(UserEntity userEntity, Model model) {

        UserEntity update = userService.update(userEntity);
        model.addAttribute("updated", "ok");
        return "success-message-page";
    }

    @SecuredForAdmin
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable long id, Model model) {

        userService.deleteById(id);
        model.addAttribute("updated", "ok");
        return "success-message-page";
    }
}
