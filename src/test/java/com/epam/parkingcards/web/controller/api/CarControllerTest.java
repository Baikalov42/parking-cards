package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.dao.CarDao;
import com.epam.parkingcards.dao.ModelDao;
import com.epam.parkingcards.dao.UserDao;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.model.CarEntity;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.model.RoleEntity;
import com.epam.parkingcards.model.UserEntity;
import com.epam.parkingcards.web.controller.ExceptionController;
import com.epam.parkingcards.web.request.CarCreateRequest;
import com.epam.parkingcards.web.request.CarUpdateRequest;
import com.epam.parkingcards.web.response.CarResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class CarControllerTest {

    private static final long ONE = 1;
    private static final Pageable PAGEABLE =
            PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Autowired
    private CarController carController;

    @MockBean
    private CarDao carDao;
    @MockBean
    private UserDao userDao;
    @MockBean
    private ModelDao modelDao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(carController)
                .setControllerAdvice(new ExceptionController())
                .build();
    }

    @Test
    @WithMockUser(username = "name", roles = "admin")
    void create_ShouldReturnId_WhenInputDataIsValid() throws Exception {


        when(carDao.saveAndFlush(getCarEntityToDb())).thenReturn(getCarEntityFromDb());
        when(userDao.existsById(ONE)).thenReturn(true);
        when(modelDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb()));

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Success, new car id = 1"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_500_WhenLicensePlateNotValid() throws Exception {

        CarCreateRequest notValidRequest = getCarCreateRequest();
        notValidRequest.setLicensePlate("111");

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notValidRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_500_WhenUserIdNotValid() throws Exception {
        CarCreateRequest notValidRequest = getCarCreateRequest();
        notValidRequest.setUserId(-1);

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notValidRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_500_WhenModelIdNotValid() throws Exception {
        CarCreateRequest notValidRequest = getCarCreateRequest();
        notValidRequest.setModelId(-1);

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notValidRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_204_WhenUserIdNotExist() throws Exception {

        when(userDao.findById(ONE)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_204_WhenModelIdNotExist() throws Exception {

        when(modelDao.findById(ONE)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_400_WhenModelIdIsDeleted() throws Exception {

        ModelEntity deleted = getModelEntityFromDb();
        deleted.setDeleted(true);

        when(modelDao.findById(ONE)).thenReturn(Optional.of(deleted));

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnCarResponse_WhenInputId_IsValidAndExist() throws Exception {

        when(carDao.findById(ONE)).thenReturn(Optional.of(getCarEntityFromDb()));

        mockMvc.perform(get("/api/cars/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getCarResponse())));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_204_WhenInputId_NotExist() throws Exception {

        when(carDao.findById(ONE)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cars/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_400_WhenInputId_NotValid() throws Exception {
        mockMvc.perform(get("/api/cars/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAll_ShouldReturnListOfResult_WhenDataExist() throws Exception {

        List<CarResponse> list = Collections.singletonList(getCarResponse());

        when(carDao.findAll(PAGEABLE))
                .thenReturn(new PageImpl<>(Collections.singletonList(getCarEntityFromDb())));

        mockMvc.perform(get("/api/cars/page/0"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAll_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {

        when(carDao.findAll(PAGEABLE))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/cars/page/0"))
                .andExpect(status().isNoContent());
    }


    @Test
    @WithMockUser(roles = "admin")
    void getByUserId_ShouldReturnStatus_200_WhenResultIsEmpty() throws Exception {
        when(carDao.findByUserId(ONE)).thenReturn(Collections.emptyList());
        when(userDao.existsById(ONE)).thenReturn(true);

        mockMvc.perform(
                get("/api/cars/by-user-id/1"))
                .andExpect(content().json(mapper.writeValueAsString(Collections.emptyList())))
                .andExpect(status().isOk());
    }

    //TODO исправить метод проверки user id
    @Test
    @WithMockUser(roles = "admin")
    void getByUserId_ShouldReturnStatus_204_WhenUserIdNotExist() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnResponse_WhenDataIsValid() throws Exception {

        when(carDao.updateCarWithoutUserId(getCarEntityFromDb())).thenReturn(getCarEntityFromDb());
        when(userDao.existsById(ONE)).thenReturn(true);
        when(carDao.existsById(ONE)).thenReturn(true);
        when(modelDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb()));

        mockMvc.perform(put("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getCarResponse())));
    }

    @Test
    @WithMockUser(username = "test@test.test")
    void update_ShouldReturnResponse_WhenCarBelongUser() throws Exception {
        UserEntity userEntityFromDb = getUserEntityFromDb();
        userEntityFromDb.getCarEntities().add(getCarEntityFromDb());

        when(userDao.findByEmail("test@test.test")).thenReturn(Optional.of(userEntityFromDb));
        when(carDao.updateCarWithoutUserId(getCarEntityFromDb())).thenReturn(getCarEntityFromDb());
        when(userDao.existsById(ONE)).thenReturn(true);
        when(carDao.existsById(ONE)).thenReturn(true);
        when(modelDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb()));

        mockMvc.perform(put("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getCarResponse())));
    }

    @Test
    @WithMockUser(username = "test@test.test")
    void update_ShouldReturnStatus_500_When_CarUserId_NotOwn() throws Exception {

        CarUpdateRequest updating = new CarUpdateRequest();
        updating.setUserId(2);

        when(userDao.findByEmail("test@test.test")).thenReturn(Optional.of(getUserEntityFromDb()));

        mockMvc.perform(put("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(username = "test@test.test")
    void update_ShouldReturnStatus_500_WhenCarNotBelongUser() throws Exception {

        CarUpdateRequest updating = new CarUpdateRequest();
        updating.setId(2);

        when(userDao.findByEmail("test@test.test")).thenReturn(Optional.of(getUserEntityFromDb()));

        mockMvc.perform(put("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }


    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_400_WhenUserIdNotExist() throws Exception {

        when(userDao.existsById(ONE)).thenReturn(false);
        when(carDao.existsById(ONE)).thenReturn(true);
        when(modelDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb()));

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_204_WhenModelIdNotExist() throws Exception {

        when(userDao.existsById(ONE)).thenReturn(true);
        when(carDao.existsById(ONE)).thenReturn(true);
        when(modelDao.findById(ONE)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_400_WhenModelIdAlreadyDeleted() throws Exception {
        ModelEntity deleted = getModelEntityFromDb();
        deleted.setDeleted(true);

        when(userDao.existsById(ONE)).thenReturn(true);
        when(carDao.existsById(ONE)).thenReturn(true);
        when(modelDao.findById(ONE)).thenReturn(Optional.of(deleted));

        mockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getCarUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_200_WhenCarWasDeleted() throws Exception {

        when(carDao.existsById(ONE)).thenReturn(true);
        when(carDao.findById(ONE)).thenReturn(Optional.of(getCarEntityFromDb()));
        doNothing().when(carDao).deleteById(ONE);

        mockMvc.perform(delete("/api/cars/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_400_WhenCarNotExist() throws Exception {

        when(carDao.existsById(ONE)).thenReturn(false);

        mockMvc.perform(delete("/api/cars/1"))
                .andExpect(status().isBadRequest());
    }

    private static CarResponse getCarResponse() {

        CarResponse carResponse = new CarResponse();

        carResponse.setId(ONE);
        carResponse.setLicensePlate("A123AA000");
        carResponse.setModelId(ONE);
        carResponse.setUserId(ONE);

        return carResponse;
    }

    private static CarCreateRequest getCarCreateRequest() {

        CarCreateRequest carCreateRequest = new CarCreateRequest();

        carCreateRequest.setLicensePlate("A123AA000");
        carCreateRequest.setModelId(ONE);
        carCreateRequest.setUserId(ONE);

        return carCreateRequest;
    }

    private static CarUpdateRequest getCarUpdateRequest() {
        CarUpdateRequest carUpdateRequest = new CarUpdateRequest();

        carUpdateRequest.setId(ONE);
        carUpdateRequest.setLicensePlate("A123AA000");
        carUpdateRequest.setModelId(ONE);
        carUpdateRequest.setUserId(ONE);

        return carUpdateRequest;
    }

    private static CarEntity getCarEntityFromDb() {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName("Testname");
        userEntity.setLastName("Testlastname");
        userEntity.setEmail("test@test.test");
        userEntity.setId(ONE);
        userEntity.setPhone("+74352784000");
        userEntity.setPassword("testtest");

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(ONE);
        brandEntity.setName("Testname");

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(ONE);
        modelEntity.setName("Testname");
        modelEntity.setBrandEntity(brandEntity);

        CarEntity carEntity = new CarEntity();
        carEntity.setId(ONE);
        carEntity.setLicensePlate("A123AA000");
        carEntity.setModelEntity(modelEntity);
        carEntity.setUserEntity(userEntity);

        return carEntity;
    }

    private static ModelEntity getModelEntityFromDb() {

        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(ONE);
        brandEntity.setName("Testname");

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(ONE);
        modelEntity.setName("Testname");
        modelEntity.setBrandEntity(brandEntity);

        return modelEntity;
    }

    private static UserEntity getUserEntityFromDb() {
        UserEntity userEntity = new UserEntity();

        userEntity.setFirstName("Testname");
        userEntity.setLastName("Testlastname");
        userEntity.setEmail("test@test.test");
        userEntity.setId(ONE);
        userEntity.setPhone("+74352784000");
        userEntity.setPassword("testtest");

        return userEntity;
    }

    private static CarEntity getCarEntityToDb() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(ONE);

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(ONE);

        CarEntity carEntity = new CarEntity();
        carEntity.setLicensePlate("A123AA000");
        carEntity.setModelEntity(modelEntity);
        carEntity.setUserEntity(userEntity);

        return carEntity;
    }

    private static RoleEntity getRoleUser() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(ONE);
        roleEntity.setName("ROLE_user");

        return roleEntity;
    }
}