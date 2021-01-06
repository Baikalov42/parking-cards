package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.dao.BrandDao;
import com.epam.parkingcards.dao.ModelDao;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.service.BrandService;
import com.epam.parkingcards.service.ModelService;
import com.epam.parkingcards.web.controller.ExceptionController;
import com.epam.parkingcards.web.request.ModelCreateRequest;
import com.epam.parkingcards.web.request.ModelUpdateRequest;
import com.epam.parkingcards.web.response.ModelResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
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
class ModelControllerTest {

    private static final long ONE = 1;

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Autowired
    @InjectMocks
    private ModelController modelController;

    @Autowired
    @InjectMocks
    private ModelService modelService;

    @Autowired
    @InjectMocks
    private BrandService brandService;

    @Mock
    private ModelDao modelDao;
    @Mock
    private BrandDao brandDao;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(modelController)
                .setControllerAdvice(new ExceptionController())
                .build();
    }

    @Test
    @WithMockUser(username = "name", roles = {"admin"})
    void create_ShouldReturnBrandId_WhenInputDataIsValid() throws Exception {
        when(modelDao.saveAndFlush(getModelEntityToDb())).thenReturn(getModelEntityFromDb());
        when(modelDao.getCountDeletedByName(getModelEntityToDb().getName())).thenReturn(0L);
        when(brandDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb().getBrandEntity()));

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Success, new model id = 1"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_500_WhenNameNotValid() throws Exception {

        ModelCreateRequest notValidRequest = getModelCreateRequest();
        notValidRequest.setName("Not@@ValidName");

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notValidRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_500_WhenNameAlreadyExist() throws Exception {

        when(modelDao.getCountDeletedByName(getModelEntityToDb().getName())).thenReturn(0L);
        when(brandDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb().getBrandEntity()));

        Mockito.when(modelDao.saveAndFlush(getModelEntityToDb()))
                .thenThrow(TransientDataAccessException.class);

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_400_WhenNameAlreadyExistAndDeleted() throws Exception {

        Mockito.when(modelDao.getCountDeletedByName(getModelCreateRequest().getName()))
                .thenReturn(ONE);

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_500_WhenBrandIdNotValid() throws Exception {
        ModelCreateRequest notValidRequest = getModelCreateRequest();
        notValidRequest.setBrandId(-1);

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notValidRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_204_WhenBrandIdNotExist() throws Exception {

        Mockito.when(brandDao.findById(ONE)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(roles = "user")
    @Test
    void create_ShouldReturnStatus_500_WhenRoleIsUser() throws Exception {

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnModelResponse_WhenInputId_IsValidAndExist() throws Exception {
        Mockito.when(modelDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb()));

        mockMvc.perform(get("/api/models/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getModelResponse())));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_204_WhenInputId_NotExist() throws Exception {

    }


    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_400_WhenInputId_NotValid() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllActive_ShouldReturnListOfResult_WhenDataExist() throws Exception {


    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllActive_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllDeleted_ShouldReturnListOfResult_WhenDataExist() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllDeleted_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void getByBrand_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void getByBrand_ShouldReturnStatus_204_WhenBrandIdNotExist() throws Exception {

    }

    @Test
    @WithMockUser(username = "name", roles = {"admin", "user"})
    void searchByPart_ShouldReturnListOfResult_WhenDataExist() throws Exception {

    }

    @Test
    @WithMockUser(username = "name", roles = {"admin", "user"})
    void searchByPart_ShouldReturnEmptyList_WhenDataNotExist() throws Exception {

    }

    @Test
    @WithMockUser(username = "name", roles = {"admin"})
    void update_ShouldReturnResponse_WhenDataIsValid() throws Exception {

    }

    @Test
    @WithMockUser(username = "name", roles = {"user"})
    void update_ShouldReturnStatus_500_WhenRoleIsUser() throws Exception {


    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_400_WhenNameAlreadyExistAndDeleted() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_500_WhenNameNotValid() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_200_WhenBrandWasDeleted() throws Exception {

    }

    void delete_ShouldReturnStatus_500_WhenBrandNotExist() throws Exception {

    }

    void delete_ShouldReturnStatus_500_WhenBrandAlreadyDeleted() throws Exception {
    }

    private static ModelResponse getModelResponse() throws JsonProcessingException {

        ModelResponse modelResponse = new ModelResponse();

        modelResponse.setId(ONE);
        modelResponse.setName("Testname");
        modelResponse.setBrandId(ONE);
        modelResponse.setDeleted(false);

        return modelResponse;
    }

    private static ModelCreateRequest getModelCreateRequest() throws JsonProcessingException {

        ModelCreateRequest modelCreateRequest = new ModelCreateRequest();

        modelCreateRequest.setName("Testname");
        modelCreateRequest.setBrandId(ONE);

        return modelCreateRequest;
    }

    private static ModelUpdateRequest getModelUpdateRequest() throws JsonProcessingException {
        ModelUpdateRequest modelUpdateRequest = new ModelUpdateRequest();

        modelUpdateRequest.setId(ONE);
        modelUpdateRequest.setName("Testname");
        modelUpdateRequest.setBrandId(ONE);

        return modelUpdateRequest;
    }

    private static ModelEntity getModelEntityFromDb() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(ONE);
        brandEntity.setName("Testbrand");

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setId(ONE);
        modelEntity.setName("Testname");
        modelEntity.setBrandEntity(brandEntity);

        return modelEntity;
    }

    private static ModelEntity getModelEntityToDb() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(ONE);

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setName("Testname");
        modelEntity.setBrandEntity(brandEntity);

        return modelEntity;
    }
}