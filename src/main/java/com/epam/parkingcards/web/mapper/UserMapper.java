package com.epam.parkingcards.web.mapper;

import com.epam.parkingcards.web.request.UserRegistrationRequest;
import com.epam.parkingcards.web.request.admin.UserUpdateRequest;
import com.epam.parkingcards.web.request.me.MeUserUpdateRequest;
import com.epam.parkingcards.web.response.UserResponse;
import com.epam.parkingcards.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    @Autowired
    private CarMapper carMapper;

    public UserEntity toUser(UserRegistrationRequest userRegistrationRequest) {

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail(userRegistrationRequest.getEmail());
        userEntity.setFirstName(userRegistrationRequest.getFirstName());
        userEntity.setLastName(userRegistrationRequest.getLastName());
        userEntity.setPassword(userRegistrationRequest.getPassword());
        userEntity.setPhone(userRegistrationRequest.getPhone());

        return userEntity;
    }

    public UserEntity toUser(UserUpdateRequest userUpdateRequest) {

        UserEntity userEntity = new UserEntity();

        userEntity.setId(userUpdateRequest.getId());
        userEntity.setEmail(userUpdateRequest.getEmail());
        userEntity.setFirstName(userUpdateRequest.getFirstName());
        userEntity.setLastName(userUpdateRequest.getLastName());
        userEntity.setPhone(userUpdateRequest.getPhone());

        return userEntity;
    }

    public UserEntity toUser(MeUserUpdateRequest meUserUpdateRequest) {

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(meUserUpdateRequest.getEmail());
        userEntity.setFirstName(meUserUpdateRequest.getFirstName());
        userEntity.setLastName(meUserUpdateRequest.getLastName());
        userEntity.setPhone(meUserUpdateRequest.getPhone());

        return userEntity;
    }


    public UserResponse toUserResponse(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(userEntity.getId());
        userResponse.setFirstName(userEntity.getFirstName());
        userResponse.setLastName(userEntity.getLastName());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setPhone(userEntity.getPhone());

        userResponse.setCarsCount(userEntity.getCarEntities().size());

        return userResponse;
    }

    public List<UserResponse> toUserResponses(List<UserEntity> userEntities) {

        List<UserResponse> userResponses = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userResponses.add(toUserResponse(userEntity));
        }
        return userResponses;
    }
}
