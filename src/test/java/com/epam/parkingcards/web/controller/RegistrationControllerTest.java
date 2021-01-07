package com.epam.parkingcards.web.controller;

import com.epam.parkingcards.dao.RoleDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.model.RoleEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.service.RoleService;
import com.epam.parkingcards.service.UserService;
import com.epam.parkingcards.web.request.UserRegistrationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @MockBean
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
    void create_WhenInputDataIsValid() throws Exception {
        UserRegistrationRequest freshUser = new UserRegistrationRequest();
        freshUser.setFirstName("Anton");
        freshUser.setLastName("Antonov");
        freshUser.setEmail("ant@mail.ru");
        freshUser.setPhone("+74352784903");
        freshUser.setPassword("badpassword");
        freshUser.setConfirmPassword("badpassword");

        UserEntity userEntityFromDb = new UserEntity();
        userEntityFromDb.setId(1L);
        userEntityFromDb.setFirstName("Anton");
        userEntityFromDb.setLastName("Antonov");
        userEntityFromDb.setEmail("ant@mail.ru");
        userEntityFromDb.setPhone("+74352784903");
        userEntityFromDb.setPassword("badpassword");

        UserEntity userEntityToDb = new UserEntity();
        userEntityToDb.setFirstName("Anton");
        userEntityToDb.setLastName("Antonov");
        userEntityToDb.setEmail("ant@mail.ru");
        userEntityToDb.setPhone("+74352784903");
        userEntityToDb.setPassword("badpassword");

        RoleEntity role = new RoleEntity();
        role.setName("ROLE_user");
        role.setId(1L);

        Mockito.when(passwordEncoder.encode("badpassword")).thenReturn("badpassword");
        Mockito.when(roleDao.findByName("ROLE_user")).thenReturn(java.util.Optional.of(role));
        Mockito.when(userDao.save(userEntityToDb)).thenReturn(userEntityFromDb);

        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(freshUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User is registered, id = 1"));
    }
}