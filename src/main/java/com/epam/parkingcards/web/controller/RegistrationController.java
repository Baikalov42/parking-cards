package com.epam.parkingcards.web.controller;

import com.epam.parkingcards.service.UserService;
import com.epam.parkingcards.web.mapper.UserMapper;
import com.epam.parkingcards.web.request.UserRegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/register")
    public String register(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {

        long id = userService.register(userMapper.toUser(userRegistrationRequest));
        return "User is registered, id = " + id;
    }
}
