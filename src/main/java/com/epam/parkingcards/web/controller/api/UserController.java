package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.config.security.annotation.SecuredForAdmin;
import com.epam.parkingcards.web.mapper.UserMapper;
import com.epam.parkingcards.web.request.admin.UserUpdateRequest;
import com.epam.parkingcards.web.response.UserResponse;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")

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
    @SecuredForAdmin
    @GetMapping("/by-plate/{plate}")
    public UserResponse getByLicensePlate(@PathVariable String plate) {
        UserEntity userEntity = userService.findByLicensePlate(plate);
        return userMapper.toUserResponse(userEntity);
    }

    /**
     * Get all users
     */
    @SecuredForAdmin
    @GetMapping("/page/{pageNumber}")
    public List<UserResponse> getAll(@PathVariable int pageNumber) {
        return userMapper.toUserResponses(userService.findAll(pageNumber));
    }

    /**
     * Search by keyword in first name or last name
     */
    @SecuredForAdmin
    @PostMapping("/search")
    public List<UserResponse> searchByPart(@RequestParam("keyword") String keyword) {
        return userMapper.toUserResponses(userService.findByKeyword(keyword));
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
     * Set user role.
     */
    @SecuredForAdmin
    @PutMapping("/add-role/user/{userId}/role/{roleId}")
    ResponseEntity<String> addRole(@PathVariable long userId, @PathVariable long roleId) {
        userService.addRole(userId, roleId);
        return new ResponseEntity<>("Role set", HttpStatus.OK);
    }

    /**
     * Remove user role.
     */
    @SecuredForAdmin
    @PutMapping("/remove-role/user/{userId}/role/{roleId}")
    ResponseEntity<String> removeRole(@PathVariable long userId, @PathVariable long roleId) {
        userService.removeRole(userId, roleId);
        return new ResponseEntity<>("Role removed", HttpStatus.OK);
    }


    /**
     * Delete user by ID
     */
    @SecuredForAdmin
    @DeleteMapping("/{userId}")
    public void deleteById(@PathVariable long userId) {
        userService.deleteById(userId);

    }
}