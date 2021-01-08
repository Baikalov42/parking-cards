package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.dao.RoleDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.model.RoleEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.web.controller.ExceptionController;
import com.epam.parkingcards.web.request.UserUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class UserControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private UserDao userDao;
    @MockBean
    private RoleDao roleDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserController userController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new ExceptionController())
                .build();
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnUserResponse_WhenInputId_IsValidAndExist() throws Exception {
        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(getUserEntity()));
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Admin")))
                .andExpect(jsonPath("$.lastName", is("Adminov")))
                .andExpect(jsonPath("$.phone", is("+74352784903")))
                .andExpect(jsonPath("$.email", is("test@test.test")))
                .andExpect(jsonPath("$.carsCount", is(0)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_204_WhenInputId_NotExist() throws Exception {
        Mockito.when(userDao.findById(66L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/users/66"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_400_WhenInputId_NotValid() throws Exception {
        mockMvc.perform(get("/api/users/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAll_ShouldReturnListOfUsers_WhenDataExist() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Mockito.when(userDao.findAll(pageable)).
                thenReturn(new PageImpl<>(Collections.singletonList(getUserEntity())));

        mockMvc.perform(get("/api/users/page/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Admin")))
                .andExpect(jsonPath("$[0].lastName", is("Adminov")))
                .andExpect(jsonPath("$[0].phone", is("+74352784903")))
                .andExpect(jsonPath("$[0].email", is("test@test.test")))
                .andExpect(jsonPath("$[0].carsCount", is(0)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAll_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Mockito.when(userDao.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        mockMvc.perform(get("/api/users/page/0"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void searchUserByLicensePlate_ShouldReturnUser_WhenDataValid() throws Exception {
        UserEntity user = getUserEntity();

        CarEntity car = new CarEntity();
        car.setLicensePlate("A123BC178");
        car.setId(1L);
        car.setUserEntity(user);

        Set<CarEntity> cars = new HashSet<>();
        cars.add(car);

        user.setCarEntities(cars);

        Mockito.when(userDao.findByLicensePlate("A123BC178")).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/users/by-plate/A123BC178"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Admin")))
                .andExpect(jsonPath("$.lastName", is("Adminov")))
                .andExpect(jsonPath("$.phone", is("+74352784903")))
                .andExpect(jsonPath("$.email", is("test@test.test")))
                .andExpect(jsonPath("$.carsCount", is(1)));
    }

    @Test
    @WithMockUser(username = "name", roles = {"admin", "user"})
    void searchByPart_ShouldReturnListOfResult_WhenDataExist() throws Exception {
        Mockito.when(userDao.findByKeyword("Adm".toLowerCase())).thenReturn(Collections.singletonList(getUserEntity()));
        mockMvc.perform(post("/api/users/search")
                .param("keyword", "adm"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("Admin")))
                .andExpect(jsonPath("$[0].lastName", is("Adminov")))
                .andExpect(jsonPath("$[0].phone", is("+74352784903")))
                .andExpect(jsonPath("$[0].email", is("test@test.test")))
                .andExpect(jsonPath("$[0].carsCount", is(0)));
    }

    @Test
    @WithMockUser(username = "name", roles = {"admin", "user"})
    void searchByPart_ShouldReturnEmptyList_WhenDataNotExist() throws Exception {
        Mockito.when(userDao.findByKeyword("ZYW".toLowerCase())).thenReturn(Collections.emptyList());
        mockMvc.perform(post("/api/users/search")
                .param("keyword", "ZYW"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnResponse_WhenDataIsValid() throws Exception {

        UserEntity userUpdateEntity = new UserEntity();
        userUpdateEntity.setId(1L);
        userUpdateEntity.setFirstName("Admin");
        userUpdateEntity.setLastName("Adminov");
        userUpdateEntity.setEmail("test@test.test");
        userUpdateEntity.setPhone("+74352784903");

        UserEntity updatedUserFromBd = new UserEntity();
        updatedUserFromBd.setId(1L);
        updatedUserFromBd.setFirstName("Admin");
        updatedUserFromBd.setLastName("Adminov");
        updatedUserFromBd.setEmail("test@test.test");
        updatedUserFromBd.setPhone("+74352784903");
        updatedUserFromBd.setPassword("oldPass_oldRole_oldCars");

        Mockito.when(userDao.existsById(1L)).thenReturn(true);
        Mockito.when(userDao.updateWithoutPasswordAndCars(userUpdateEntity)).thenReturn(updatedUserFromBd);
        mockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Admin")))
                .andExpect(jsonPath("$.lastName", is("Adminov")))
                .andExpect(jsonPath("$.phone", is("+74352784903")))
                .andExpect(jsonPath("$.email", is("test@test.test")))
                .andExpect(jsonPath("$.carsCount", is(0)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_400_WhenUserNotExist() throws Exception {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(1L);
        userUpdateRequest.setFirstName("Admin");
        userUpdateRequest.setLastName("Adminov");
        userUpdateRequest.setEmail("test@test.test");
        userUpdateRequest.setPhone("+74352784903");

        Mockito.when(userDao.existsById(1L)).thenReturn(false);
        mockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userUpdateRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "test@test.test")
    void update_UserByHimself_ShouldReturnResponse_200_WhenDataIsValid() throws Exception {

        UserEntity userUpdateEntity = new UserEntity();
        userUpdateEntity.setId(1L);
        userUpdateEntity.setFirstName("Admin");
        userUpdateEntity.setLastName("Adminov");
        userUpdateEntity.setEmail("test@test.test");
        userUpdateEntity.setPhone("+74352784903");

        UserEntity updatedUserFromBd = new UserEntity();
        updatedUserFromBd.setId(1L);
        updatedUserFromBd.setFirstName("Admin");
        updatedUserFromBd.setLastName("Adminov");
        updatedUserFromBd.setEmail("test@test.test");
        updatedUserFromBd.setPhone("+74352784903");
        updatedUserFromBd.setPassword("oldPass_oldRole_oldCars");

        when(userDao.findByEmail("test@test.test")).thenReturn(Optional.of(userUpdateEntity));

        Mockito.when(userDao.existsById(1L)).thenReturn(true);
        Mockito.when(userDao.updateWithoutPasswordAndCars(userUpdateEntity)).thenReturn(updatedUserFromBd);
        mockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Admin")))
                .andExpect(jsonPath("$.lastName", is("Adminov")))
                .andExpect(jsonPath("$.phone", is("+74352784903")))
                .andExpect(jsonPath("$.email", is("test@test.test")))
                .andExpect(jsonPath("$.carsCount", is(0)));
    }

    @Test
    @WithMockUser(username = "test@test.test")
    void update_UserByAnotherUser_ShouldReturn_500() throws Exception {

        UserEntity userUpdateEntity = new UserEntity();
        userUpdateEntity.setId(2L);
        userUpdateEntity.setFirstName("Admin");
        userUpdateEntity.setLastName("Adminov");
        userUpdateEntity.setEmail("test@test.test");
        userUpdateEntity.setPhone("+74352784903");

        UserUpdateRequest anotherUser = getUserUpdateRequest();
        anotherUser.setId(2L);

        UserEntity updatedUserFromBd = new UserEntity();
        updatedUserFromBd.setId(1L);
        updatedUserFromBd.setFirstName("Admin");
        updatedUserFromBd.setLastName("Adminov");
        updatedUserFromBd.setEmail("test@test.test");
        updatedUserFromBd.setPhone("+74352784903");
        updatedUserFromBd.setPassword("oldPass_oldRole_oldCars");

        when(userDao.findByEmail("test@test.test")).thenReturn(Optional.of(userUpdateEntity));

        Mockito.when(userDao.existsById(1L)).thenReturn(true);
        Mockito.when(userDao.updateWithoutPasswordAndCars(userUpdateEntity)).thenReturn(updatedUserFromBd);
        mockMvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getUserUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void addRole_ToUser_ShouldReturnStatus_200_WhenDataValid() throws Exception {

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Admin");
        userEntity.setLastName("Adminov");
        userEntity.setEmail("test@test.test");
        userEntity.setPhone("+74352784903");
        userEntity.setPassword("admin");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ROLE_user");

        Mockito.when(userDao.existsById(1L)).thenReturn(true);
        Mockito.when(roleDao.existsById(1L)).thenReturn(true);

        Mockito.when(userDao.findById(1L)).thenReturn(Optional.of(userEntity));
        Mockito.when(roleDao.getOne(1L)).thenReturn(roleEntity);

        userEntity.getRoleEntities().add(roleEntity);

        Mockito.when(userDao.saveAndFlush(userEntity)).thenReturn(userEntity);
        mockMvc.perform(put("/api/users/add-role/user/1/role/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Role set"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void removeRole_FromUser_ShouldReturnStatus_200_WhenDataValid() throws Exception {
        UserEntity userEntity = getUserEntity();

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("ROLE_user");

        Mockito.when(userDao.existsById(1L)).thenReturn(true);
        Mockito.when(roleDao.existsById(1L)).thenReturn(true);
        Mockito.when(userDao.getOne(1L)).thenReturn(userEntity);
        Mockito.when(roleDao.getOne(1L)).thenReturn(roleEntity);

        Mockito.when(userDao.saveAndFlush(userEntity)).thenReturn(userEntity);
        mockMvc.perform(put("/api/users/remove-role/user/1/role/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Role removed"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_200_WhenUserExist() throws Exception {
        Mockito.when(userDao.existsById(1L)).thenReturn(true);
        doNothing().when(userDao).deleteById(1L);
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_400_WhenUserNotExist() throws Exception {
        Mockito.when(userDao.existsById(11L)).thenReturn(false);
        mockMvc.perform(delete("/api/users/11"))
                .andExpect(status().isBadRequest());
    }

    static UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Admin");
        userEntity.setLastName("Adminov");
        userEntity.setEmail("test@test.test");
        userEntity.setPhone("+74352784903");
        userEntity.setPassword("admin");
        return userEntity;
    }

    static UserUpdateRequest getUserUpdateRequest() {
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setId(1L);
        userUpdateRequest.setFirstName("Admin");
        userUpdateRequest.setLastName("Adminov");
        userUpdateRequest.setEmail("test@test.test");
        userUpdateRequest.setPhone("+74352784903");
        return userUpdateRequest;
    }
}
