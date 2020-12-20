package com.epam.parkingcards.web.mapper;

import com.epam.parkingcards.web.request.UserRegistrationRequest;
import com.epam.parkingcards.web.request.admin.UserUpdateRequest;
import com.epam.parkingcards.web.request.me.MeUserUpdateRequest;
import com.epam.parkingcards.web.response.UserResponse;
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

    public User toUser(UserRegistrationRequest userRegistrationRequest) {

        User user = new User();
        user.setEmail(userRegistrationRequest.getEmail());
        user.setFirstName(userRegistrationRequest.getFirstName());
        user.setLastName(userRegistrationRequest.getLastName());
        user.setPassword(userRegistrationRequest.getPassword());
        user.setPhone(userRegistrationRequest.getPhone());

        return user;
    }

    public User toUser(UserUpdateRequest userUpdateRequest) {

        User user = new User();
        user.setId(userUpdateRequest.getId());
        user.setEmail(userUpdateRequest.getEmail());
        user.setFirstName(userUpdateRequest.getFirstName());
        user.setLastName(userUpdateRequest.getLastName());
        user.setPhone(userUpdateRequest.getPhone());

        return user;
    }

    public User toUser(MeUserUpdateRequest meUserUpdateRequest) {

        User user = new User();
        user.setEmail(meUserUpdateRequest.getEmail());
        user.setFirstName(meUserUpdateRequest.getFirstName());
        user.setLastName(meUserUpdateRequest.getLastName());
        user.setPhone(meUserUpdateRequest.getPhone());

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
