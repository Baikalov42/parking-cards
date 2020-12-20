package com.epam.parkingcards.controller.mapper;

import com.epam.parkingcards.controller.request.UserCreateRequest;
import com.epam.parkingcards.controller.request.UserRequest;
import com.epam.parkingcards.controller.response.UserResponse;
import com.epam.parkingcards.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private CarMapper carMapper;

    public User toUser(UserCreateRequest userCreateRequest) {

        User user = new User();
        user.setEmail(userCreateRequest.getEmail());
        user.setFirstName(userCreateRequest.getFirstName());
        user.setLastName(userCreateRequest.getLastName());
        user.setPassword(userCreateRequest.getPassword());
        user.setPhone(userCreateRequest.getPhone());

        return user;
    }

    public User toUser(UserRequest userRequest) {

        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhone(userRequest.getPhone());

        return user;
    }


    public UserResponse toUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPhone(user.getPhone());

        userResponse.setCars(user.getCars().stream()
                .map(x -> carMapper.toCarResponse(x))
                .collect(Collectors.toSet()));

        return userResponse;
    }

    public List<UserResponse> toUserResponses(List<User> users) {

        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponses.add(toUserResponse(user));
        }
        return userResponses;
    }
}