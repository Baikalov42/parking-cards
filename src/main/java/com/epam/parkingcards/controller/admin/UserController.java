package com.epam.parkingcards.controller.admin;

import com.epam.parkingcards.controller.mapper.UserMapper;
import com.epam.parkingcards.controller.request.UserCreateRequest;
import com.epam.parkingcards.controller.request.UserRequest;
import com.epam.parkingcards.controller.response.UserResponse;
import com.epam.parkingcards.model.User;
import com.epam.parkingcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")

public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;

    /**
     * Get all users
     */
    @GetMapping("/page/{pageNumber}")
    public List<UserResponse> getAllUsers(@PathVariable int pageNumber) {
        return userMapper.toUserResponses(userService.findAll(pageNumber));
    }

    /**
     * Get user by id
     */
    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable long userId) {
        User user = userService.findById(userId);
        return userMapper.toUserResponse(user);
    }

    /**
     * Get user by plate
     */
    @GetMapping("/by-plate/{plate}")
    public UserResponse getByLicensePlate(@PathVariable String plate) {
        User user = userService.findByLicensePlate(plate);
        return userMapper.toUserResponse(user);
    }

    /**
     * Create user
     */
    @PostMapping()
    public String register(@RequestBody @Valid UserCreateRequest userCreateRequest) {
        long id = userService.register(userMapper.toUser(userCreateRequest));
        return String.valueOf(id);
    }

    /**
     * Update user
     */
    @PutMapping()
    public UserResponse update(@RequestBody @Valid UserRequest userRequest) {
        User updated = userService.update(userMapper.toUser(userRequest));
        return userMapper.toUserResponse(updated);
    }

    /**
     * Delete user by ID
     */
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteById(userId);
    }
}