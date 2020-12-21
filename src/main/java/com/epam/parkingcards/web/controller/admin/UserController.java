package com.epam.parkingcards.web.controller.admin;

import com.epam.parkingcards.web.mapper.UserMapper;
import com.epam.parkingcards.web.request.admin.UserUpdateRequest;
import com.epam.parkingcards.web.response.UserResponse;
import com.epam.parkingcards.model.UserEntity;
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
        UserEntity userEntity = userService.findById(userId);
        return userMapper.toUserResponse(userEntity);
    }

    /**
     * Get user by plate
     */
    @GetMapping("/by-plate/{plate}")
    public UserResponse getByLicensePlate(@PathVariable String plate) {
        UserEntity userEntity = userService.findByLicensePlate(plate);
        return userMapper.toUserResponse(userEntity);
    }

    /**
     * Update user
     */
    @PutMapping()
    public UserResponse update(@RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        UserEntity updated = userService.update(userMapper.toUser(userUpdateRequest));
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