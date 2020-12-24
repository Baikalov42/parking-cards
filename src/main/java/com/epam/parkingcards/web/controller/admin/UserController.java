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
     * Get user by id
     */
    @GetMapping("/{userId}")
    public UserResponse getById(@PathVariable long userId) {
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
     * Get all users
     */
    @GetMapping("/page/{pageNumber}")
    public List<UserResponse> getAll(@PathVariable int pageNumber) {
        return userMapper.toUserResponses(userService.findAll(pageNumber));
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
    public void deleteById(@PathVariable long userId) {
        userService.deleteById(userId);

    }
    /**
     * Search by keyword in first name or last name
     */
    @PostMapping("/searchByKeyword")
    public List<UserResponse> searchByPart(@RequestParam("keyword") String keyword) {
        return userMapper.toUserResponses(userService.findByKeyword(keyword));
    }
}