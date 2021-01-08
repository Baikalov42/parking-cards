package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.dao.BrandDao;
import com.epam.parkingcards.dao.ModelDao;
import com.epam.parkingcards.exception.DaoException;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.model.ModelEntity;
import com.epam.parkingcards.web.controller.ExceptionController;
import com.epam.parkingcards.web.request.ModelCreateRequest;
import com.epam.parkingcards.web.request.ModelUpdateRequest;
import com.epam.parkingcards.web.response.ModelResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
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
class ModelControllerTest {

    private static final long ONE = 1;
    private static final Pageable PAGEABLE =
            PageRequest.of(0, 10, Sort.Direction.ASC, "id");

    @Autowired
    private ObjectMapper mapper;

    private MockMvc mockMvc;

    @Autowired
    private ModelController modelController;

    @MockBean
    private ModelDao modelDao;
    @MockBean
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
    void create_ShouldReturnStatus_500_WhenNameNotUnique() throws Exception {

        when(modelDao.getCountDeletedByName(getModelEntityToDb().getName())).thenReturn(0L);
        when(brandDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb().getBrandEntity()));

        when(modelDao.saveAndFlush(getModelEntityToDb()))
                .thenThrow(DuplicateKeyException.class);

        mockMvc.perform(post("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelCreateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_400_WhenNameAlreadyExistAndDeleted() throws Exception {

        when(modelDao.getCountDeletedByName(getModelCreateRequest().getName()))
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

        when(brandDao.findById(ONE)).thenReturn(Optional.empty());

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
        when(modelDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb()));

        mockMvc.perform(get("/api/models/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getModelResponse())));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_204_WhenInputId_NotExist() throws Exception {

        when(modelDao.findById(ONE)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/models/1"))
                .andExpect(status().isNoContent());
    }


    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_400_WhenInputId_NotValid() throws Exception {
        mockMvc.perform(get("/api/models/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllActive_ShouldReturnListOfResult_WhenDataExist() throws Exception {

        List<ModelResponse> list = Collections.singletonList(getModelResponse());

        when(modelDao.findByIsDeletedFalse(PAGEABLE))
                .thenReturn(new PageImpl<>(Collections.singletonList(getModelEntityFromDb())));

        mockMvc.perform(get("/api/models/page/0"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllActive_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {

        when(modelDao.findByIsDeletedFalse(PAGEABLE))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/models/page/0"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllDeleted_ShouldReturnListOfResult_WhenDataExist() throws Exception {
        List<ModelResponse> list = Collections.singletonList(getModelResponse());

        when(modelDao.findAllDeleted(PAGEABLE))
                .thenReturn(new PageImpl<>(Collections.singletonList(getModelEntityFromDb())));

        mockMvc.perform(get("/api/models/deleted/page/0"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(list)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllDeleted_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {

        when(modelDao.findAllDeleted(PAGEABLE)).thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/models/deleted/page/0"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getByBrand_ShouldReturnStatus_204_WhenResultIsEmpty() throws Exception {
        when(modelDao.findByBrandId(ONE, PAGEABLE))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(
                get("/api/models/get-by-brand/{brandId}/page/{pageNumber}", "1", "0"))
                .andExpect(status().isNoContent());
    }

    //TODO исправить метод проверки brand id
    @Test
    @WithMockUser(roles = "admin")
    void getByBrand_ShouldReturnStatus_204_WhenBrandIdNotExist() throws Exception {

    }

    @Test
    @WithMockUser(roles = "admin")
    void searchByPart_ShouldReturnListOfResult_WhenDataExist() throws Exception {

        List<ModelEntity> list = Collections.singletonList(getModelEntityFromDb());
        List<ModelResponse> responses = Collections.singletonList(getModelResponse());

        when(modelDao.findByKeyword("tes")).thenReturn(list);

        mockMvc.perform(post("/api/models/search")
                .param("keyword", "tes"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responses)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void searchByPart_ShouldReturnEmptyList_WhenDataNotExist() throws Exception {

        when(modelDao.findByKeyword("tes")).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/models/search")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("keyword", "Tes"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnResponse_WhenDataIsValid() throws Exception {

        when(modelDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb()));
        when(brandDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb().getBrandEntity()));
        when(modelDao.getCountDeletedByName("Testname")).thenReturn(0L);
        when(modelDao.saveAndFlush(getModelEntityFromDb())).thenReturn(getModelEntityFromDb());

        mockMvc.perform(put("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(getModelResponse())));
    }

    @Test
    @WithMockUser(username = "name", roles = "user")
    void update_ShouldReturnStatus_500_WhenRoleIsUser() throws Exception {

        mockMvc.perform(put("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_400_WhenNameAlreadyExistAndDeleted() throws Exception {
        ModelEntity deletedModel = getModelEntityFromDb();
        deletedModel.setDeleted(true);

        when(modelDao.findById(ONE)).thenReturn(Optional.of(deletedModel));

        mockMvc.perform(put("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(getModelUpdateRequest()))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_500_WhenNameNotValid() throws Exception {
        ModelEntity notValidNameModel = getModelEntityFromDb();
        notValidNameModel.setName("NOT@VALID");

        mockMvc.perform(put("/api/models")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(notValidNameModel))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_200_WhenBrandWasDeleted() throws Exception {

        doNothing().when(modelDao).deleteById(ONE);
        when(modelDao.findById(ONE)).thenReturn(Optional.of(getModelEntityFromDb()));

        mockMvc.perform(delete("/api/models/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_500_WhenBrandNotExist() throws Exception {

        when(modelDao.findById(ONE)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/models/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_400_WhenBrandAlreadyDeleted() throws Exception {
        ModelEntity deletedModel = getModelEntityFromDb();
        deletedModel.setDeleted(true);

        when(modelDao.findById(ONE)).thenReturn(Optional.of(deletedModel));

        mockMvc.perform(delete("/api/models/1"))
                .andExpect(status().isBadRequest());
    }

    private static ModelResponse getModelResponse() {

        ModelResponse modelResponse = new ModelResponse();

        modelResponse.setId(ONE);
        modelResponse.setName("Testname");
        modelResponse.setBrandId(ONE);
        modelResponse.setDeleted(false);

        return modelResponse;
    }

    private static ModelCreateRequest getModelCreateRequest() {

        ModelCreateRequest modelCreateRequest = new ModelCreateRequest();

        modelCreateRequest.setName("Testname");
        modelCreateRequest.setBrandId(ONE);

        return modelCreateRequest;
    }

    private static ModelUpdateRequest getModelUpdateRequest() {
        ModelUpdateRequest modelUpdateRequest = new ModelUpdateRequest();

        modelUpdateRequest.setId(ONE);
        modelUpdateRequest.setName("Testname");
        modelUpdateRequest.setBrandId(ONE);

        return modelUpdateRequest;
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

    private static ModelEntity getModelEntityToDb() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setId(ONE);

        ModelEntity modelEntity = new ModelEntity();
        modelEntity.setName("Testname");
        modelEntity.setBrandEntity(brandEntity);

        return modelEntity;
    }
}