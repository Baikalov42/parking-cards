package com.epam.parkingcards.web.controller;

import com.epam.parkingcards.dao.RoleDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.model.RoleEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.RoleService;
import com.epam.parkingcards.service.UserService;
import com.epam.parkingcards.web.controller.api.UserController;
import com.epam.parkingcards.web.request.UserRegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
                            //TODO        не получилось
@SpringBootTest
@ActiveProfiles("test")
class RegistrationControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private UserDao userDao;
    @MockBean
    private RoleDao roleDao;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .setControllerAdvice(new ExceptionController())
                .build();
    }

    @Test
 //   @WithMockUser(roles = "admin")
    void create_WhenInputDataIsValid() throws Exception {
        UserRegistrationRequest freshUser = new UserRegistrationRequest();
        freshUser.setFirstName("Anton");
        freshUser.setLastName("Antonov");
        freshUser.setEmail("ant@mail.ru");
        freshUser.setPhone("+74352784903");
        freshUser.setPassword("badpassword");
        freshUser.setConfirmPassword("badpassword");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Anton");
        userEntity.setLastName("Antonov");
        userEntity.setEmail("ant@mail.ru");
        userEntity.setPhone("+74352784903");
        userEntity.setPassword("badpassword");

        RoleEntity role = new RoleEntity();
        role.setName("ROLE_user");
        role.setId(1L);

        Mockito.when(roleDao.findByName("ROLE_user")).thenReturn(java.util.Optional.of(role));
        Mockito.when(userDao.save(userEntity)).thenReturn(userEntity);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(freshUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User is registered, id = 1"));
    }

}



//
//
//    @PostMapping("/register")
//    public String register(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
//
//        long id = userService.register(userMapper.toUser(userRegistrationRequest));
//        return "User is registered, id = " + id;
//    }
//    public long register(UserEntity userEntity) {
//        try {
//            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
//            userEntity.setRoleEntities(getDefaultRoles());
//            return userDao.save(userEntity).getId();
//
//        } catch (DataAccessException e) {
//            throw new DaoException(String.format("Creation error, User: %s ", userEntity), e);
//        }
//    }
//}
//
////    private String firstName;
////    private String lastName;
////    private String phone;
////    private String email;
////    private String password;
////    private String confirmPassword;