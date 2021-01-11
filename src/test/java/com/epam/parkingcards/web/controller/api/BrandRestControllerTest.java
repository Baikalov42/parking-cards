package com.epam.parkingcards.web.controller.api;

import com.epam.parkingcards.dao.BrandDao;
import com.epam.parkingcards.exception.ValidationException;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.web.request.BrandCreateRequest;
import com.epam.parkingcards.web.request.BrandUpdateRequest;
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
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class BrandRestControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private BrandDao brandDao;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private BrandRestController brandRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(brandRestController)
                .setControllerAdvice(new ExceptionRestController())
                .build();
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnBrandId_WhenInputDataIsValid() throws Exception {

        BrandCreateRequest brandCreateRequest = new BrandCreateRequest();
        brandCreateRequest.setName("Testbrand");

        BrandEntity brandToDb = new BrandEntity();
        brandToDb.setName("Testbrand");

        BrandEntity brandFromDb = new BrandEntity();
        brandFromDb.setName("Testbrand");
        brandFromDb.setId(1);

        Mockito.when(brandDao.saveAndFlush(brandToDb)).thenReturn(brandFromDb);

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(brandCreateRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Success, new brand id = 1"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_400_WhenNameNotValid() throws Exception {
        BrandCreateRequest brand = new BrandCreateRequest();
        brand.setName("Test@@brand");

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(brand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_400_WhenNameAlreadyExist() throws Exception {
        BrandCreateRequest brand = new BrandCreateRequest();
        brand.setName("Testbrand");

        Mockito.when(brandDao.getCountDeletedByName(brand.getName()))
                .thenThrow(new ValidationException("Brand already exist"));

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(brand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_400_WhenNameAlreadyExistAndDeleted() throws Exception {

        BrandCreateRequest brand = new BrandCreateRequest();
        brand.setName("Testbrand");

        Mockito.when(brandDao.getCountDeletedByName(brand.getName())).thenReturn(1L);

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(brand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @WithMockUser(roles = "user")
    @Test
    void create_ShouldReturnStatus_500_WhenRoleIsUser() throws Exception {
        BrandCreateRequest brand = new BrandCreateRequest();
        brand.setName("Testbrand");

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(brand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnBrandResponse_WhenInputId_IsValidAndExist() throws Exception {

        BrandEntity brandFromBd = new BrandEntity();
        brandFromBd.setName("Testbrand");
        brandFromBd.setId(1L);
        brandFromBd.setDeleted(false);

        Mockito.when(brandDao.findById(1L)).thenReturn(Optional.of(brandFromBd));

        mockMvc.perform(get("/api/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Testbrand")))
                .andExpect(jsonPath("$.deleted", is(false)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_204_WhenInputId_NotExist() throws Exception {

        Mockito.when(brandDao.findById(66L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/brands/66"))
                .andExpect(status().isNoContent());
    }

    //TODO какие коды возвращает Spring при ошибках валидации и какие должен?
    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnStatus_400_WhenInputId_NotValid() throws Exception {
        mockMvc.perform(get("/api/brands/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllActive_ShouldReturnListOfResult_WhenDataExist() throws Exception {

        BrandEntity testBrand2 = new BrandEntity();
        testBrand2.setId(1);
        testBrand2.setName("Testbrand");
        testBrand2.setDeleted(false);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Mockito.when(brandDao.findByIsDeletedFalse(pageable)).
                thenReturn(new PageImpl<>(Collections.singletonList(testBrand2)));

        mockMvc.perform(get("/api/brands/page/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Testbrand")))
                .andExpect(jsonPath("$[0].deleted", is(false)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllActive_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Mockito.when(brandDao.findByIsDeletedFalse(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        mockMvc.perform(get("/api/brands/page/0"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllDeleted_ShouldReturnListOfResult_WhenDataExist() throws Exception {
        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1);
        testBrand.setName("Testbrand");
        testBrand.setDeleted(true);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Mockito.when(brandDao.findAllDeleted(pageable))
                .thenReturn(new PageImpl<>(Collections.singletonList(testBrand)));

        mockMvc.perform(get("/api/brands/deleted/page/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Testbrand")))
                .andExpect(jsonPath("$[0].deleted", is(true)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllDeleted_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Mockito.when(brandDao.findAllDeleted(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        mockMvc.perform(get("/api/brands/deleted/page/0"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "name", roles = {"admin", "user"})
    void searchByPart_ShouldReturnListOfResult_WhenDataExist() throws Exception {
        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1);
        testBrand.setName("Testbrand");
        testBrand.setDeleted(false);

        Mockito.when(brandDao.findByKeyword("Tes".toLowerCase())).thenReturn(Collections.singletonList(testBrand));
        mockMvc.perform(post("/api/brands/search?keyword=tes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Testbrand")))
                .andExpect(jsonPath("$[0].deleted", is(false)));
    }

    @Test
    @WithMockUser(username = "name", roles = {"admin", "user"})
    void searchByPart_ShouldReturnEmptyList_WhenDataNotExist() throws Exception {
        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1);
        testBrand.setName("Testbrand");
        testBrand.setDeleted(false);

        Mockito.when(brandDao.findByKeyword("ZYW".toLowerCase())).thenReturn(Collections.emptyList());
        mockMvc.perform(post("/api/brands/search?keyword=ZYW"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnResponse_WhenDataIsValid() throws Exception {

        BrandUpdateRequest brandUpdateRequest = new BrandUpdateRequest();
        brandUpdateRequest.setId(1L);
        brandUpdateRequest.setName("TestTestbrand");

        BrandEntity brandBd = new BrandEntity();
        brandBd.setId(1L);
        brandBd.setName("TestTestbrand");
        brandBd.setDeleted(false);

        Mockito.when(brandDao.findById(1L)).thenReturn(Optional.of(brandBd));
        Mockito.when(brandDao.getCountDeletedByName("TestTestbrand")).thenReturn(0L);
        Mockito.when(brandDao.saveAndFlush(brandBd)).thenReturn(brandBd);

        mockMvc.perform(put("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(brandUpdateRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("TestTestbrand")))
                .andExpect(jsonPath("$.deleted", is(false)));
    }

    @Test
    @WithMockUser(roles = "user")
    void update_ShouldReturnStatus_500_WhenRoleIsUser() throws Exception {
        mockMvc.perform(put("/api/brands"))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_400_WhenNameAlreadyExistAndDeleted() throws Exception {
        BrandEntity brandBd = new BrandEntity();
        brandBd.setId(1L);
        brandBd.setName("TestTestbrand");
        brandBd.setDeleted(true);
        brandBd.setModelEntities(Collections.EMPTY_SET);

        BrandUpdateRequest testUpdateRequestBrand = new BrandUpdateRequest();
        testUpdateRequestBrand.setId(1L);
        testUpdateRequestBrand.setName("TestTestbrand");

        Mockito.when(brandDao.findById(1L)).thenReturn(Optional.of(brandBd));

        mockMvc.perform(put("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testUpdateRequestBrand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    void update_ShouldReturnStatus_400_WhenNameNotValid() throws Exception {
        BrandUpdateRequest testBrand = new BrandUpdateRequest();
        testBrand.setId(1L);
        testBrand.setName("Test@@brand");

        mockMvc.perform(put("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testBrand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_200_WhenBrandWasDeleted() throws Exception {
        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1L);
        testBrand.setName("Testbrand");
        testBrand.setDeleted(false);

        Mockito.when(brandDao.findById(1L)).thenReturn(Optional.of(testBrand));
        doNothing().when(brandDao).markAsDeleted(1L);
        mockMvc.perform(delete("/api/brands/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_204_WhenBrandNotExist() throws Exception {
        Mockito.when(brandDao.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(delete("/api/brands/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    void delete_ShouldReturnStatus_400_WhenBrandAlreadyDeleted() throws Exception {
        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1L);
        testBrand.setName("Testbrand");
        testBrand.setDeleted(true);

        Mockito.when(brandDao.findById(1L)).thenReturn(Optional.of(testBrand));
        mockMvc.perform(delete("/api/brands/1"))
                .andExpect(status().isBadRequest());
    }
}
