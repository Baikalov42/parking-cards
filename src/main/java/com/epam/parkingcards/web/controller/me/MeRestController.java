package com.epam.parkingcards.web.controller.me;

import com.epam.parkingcards.config.security.UserSecurity;
import com.epam.parkingcards.web.mapper.BrandMapper;
import com.epam.parkingcards.web.mapper.CarMapper;
import com.epam.parkingcards.web.mapper.ModelMapper;
import com.epam.parkingcards.web.mapper.UserMapper;
import com.epam.parkingcards.web.request.me.MeCarCreateRequest;
import com.epam.parkingcards.web.request.me.MeCarUpdateRequest;
import com.epam.parkingcards.web.request.me.MeUserUpdateRequest;
import com.epam.parkingcards.web.response.BrandResponse;
import com.epam.parkingcards.web.response.ModelResponse;
import com.epam.parkingcards.web.response.CarResponse;
import com.epam.parkingcards.web.response.UserResponse;
import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.BrandService;
import com.epam.parkingcards.service.ModelService;
import com.epam.parkingcards.service.CarService;
import com.epam.parkingcards.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/me")
public class MeRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ModelService modelService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private UserSecurity userSecurity;

    @GetMapping
    public UserResponse getUser(Principal principal) {
        UserEntity userEntity = userService.findByEmail(principal.getName());
        return userMapper.toUserResponse(userEntity);
    }

    @PutMapping
    public UserResponse updateUser(@Valid @RequestBody MeUserUpdateRequest meUserUpdateRequest,
                                   Authentication authentication) {

        UserEntity userEntity = userMapper.toUser(meUserUpdateRequest);
        long userId = userService.getIdByEmail(authentication.getName());
        userEntity.setId(userId);

        return userMapper.toUserResponse(userService.update(userEntity));
    }

    @GetMapping("/cars")
    public List<CarResponse> getMyCars(Principal principal) {

        UserEntity userEntity = userService.findByEmail(principal.getName());
        List<CarEntity> carEntities = new ArrayList<>(userEntity.getCarEntities());
        return carMapper.toCarResponses(carEntities);
    }

    @GetMapping("/cars/{carId}")
    public CarResponse getCarById(@PathVariable long carId, Authentication authentication) {

        if (!userSecurity.hasCar(authentication, carId)) {
            throw new ValidationException("user dont have car, with id " + carId);
        }
        return carMapper.toCarResponse(carService.findById(carId));
    }

    @PostMapping("/cars")
    public long addCar(@Valid @RequestBody MeCarCreateRequest meCarCreateRequest,
                       Authentication authentication) {

        CarEntity carEntity = carMapper.toCar(meCarCreateRequest);
        long userId = userService.getIdByEmail(authentication.getName());
        carEntity.getUserEntity().setId(userId);

        return carService.create(carEntity);
    }

    @PutMapping("/cars")
    public CarResponse updateMyCarById(@Valid @RequestBody MeCarUpdateRequest meCarUpdateRequest,
                                       Authentication authentication) {

        if (!userSecurity.hasCar(authentication, meCarUpdateRequest.getId())) {
            throw new ValidationException("user dont have car, with id " + meCarUpdateRequest.getId());
        }
        long userId = userService.getIdByEmail(authentication.getName());

        CarEntity toUpdate = carMapper.toCar(meCarUpdateRequest);
        toUpdate.getUserEntity().setId(userId);

        CarEntity updated = carService.update(toUpdate);
        return carMapper.toCarResponse(updated);
    }

    @DeleteMapping("/cars/{carId}")
    public void deleteMyCarById(@PathVariable long carId,
                                Authentication authentication) {

        if (!userSecurity.hasCar(authentication, carId)) {
            throw new ValidationException("user dont have car, with id " + carId);
        }
        carService.deleteById(carId);
    }

    @GetMapping("/models/page/{pageNumber}")
    public List<ModelResponse> getAllModels(@PathVariable int pageNumber) {
        return modelMapper.toModelResponses(modelService.findAllActive(pageNumber));
    }

    @GetMapping("/brands/page/{pageNumber}")
    public List<BrandResponse> getAllBrands(@PathVariable int pageNumber) {
        return brandMapper.toBrandResponses(brandService.findAllActive(pageNumber));
    }
}
