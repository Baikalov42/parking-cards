package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.dao.RoleDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.RoleEntity;
import com.epam.parkingcards.model.UserEntity;
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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .setControllerAdvice(new ExceptionController())
                .build();
    }

    @Test
    void register_WhenInputDataIsValid() throws Exception {
        UserRegistrationRequest freshUser = new UserRegistrationRequest();
        freshUser.setFirstName("Anton");
        freshUser.setLastName("Antonov");
        freshUser.setEmail("ant@mail.ru");
        freshUser.setPhone("+74352784903");
        freshUser.setPassword("password");
        freshUser.setConfirmPassword("password");

        UserEntity userEntityFromDb = new UserEntity();
        userEntityFromDb.setId(1L);
        userEntityFromDb.setFirstName("Anton");
        userEntityFromDb.setLastName("Antonov");
        userEntityFromDb.setEmail("ant@mail.ru");
        userEntityFromDb.setPhone("+74352784903");
        userEntityFromDb.setPassword("password");

        UserEntity userEntityToDb = new UserEntity();
        userEntityToDb.setFirstName("Anton");
        userEntityToDb.setLastName("Antonov");
        userEntityToDb.setEmail("ant@mail.ru");
        userEntityToDb.setPhone("+74352784903");
        userEntityToDb.setPassword("password");

        RoleEntity role = new RoleEntity();
        role.setName("ROLE_user");
        role.setId(1L);

        Mockito.when(passwordEncoder.encode("password")).thenReturn("password");
        Mockito.when(roleDao.findByName("ROLE_user")).thenReturn(java.util.Optional.of(role));
        Mockito.when(userDao.save(userEntityToDb)).thenReturn(userEntityFromDb);

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(freshUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User is registered, id = 1"));
    }

    @Test
    void register_WhenInputDataIsNotValid() throws Exception {
        UserRegistrationRequest freshUser = new UserRegistrationRequest();
        freshUser.setFirstName("Anton");
        freshUser.setLastName("Anton@@ov");
        freshUser.setEmail("ant@mail.ru");
        freshUser.setPhone("+74352784903");
        freshUser.setPassword("pass1word");
        freshUser.setConfirmPassword("password");

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(freshUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void register_WhenDefaultRoleNotFound() throws Exception {
        UserRegistrationRequest freshUser = new UserRegistrationRequest();
        freshUser.setFirstName("Anton");
        freshUser.setLastName("Antonov");
        freshUser.setEmail("ant@mail.ru");
        freshUser.setPhone("+74352784903");
        freshUser.setPassword("password");
        freshUser.setConfirmPassword("password");

        Mockito.when(passwordEncoder.encode("password")).thenReturn("password");
        Mockito.when(roleDao.findByName("ROLE_user")).thenThrow(new NotFoundException("Role not found"));

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(freshUser))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}