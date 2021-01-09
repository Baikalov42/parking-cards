package com.epam.parkingcards.web.controller.ui;

import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.UserService;
import com.epam.parkingcards.web.mapper.UserMapper;
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


@Controller
@RequestMapping("/ui/me")
public class MeController {

    public static final String MESSAGE_VIEW = "success-message-page";

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper mapper;

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

        UserEntity update = userService.update(mapper.toUser(request));
        model.addAttribute("message", "ok");
        return MESSAGE_VIEW;
    }

}
