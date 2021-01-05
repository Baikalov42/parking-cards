package com.epam.parkingcards.controller;

import com.epam.parkingcards.dao.BrandDao;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.service.BrandService;
import com.epam.parkingcards.web.controller.ExceptionController;
import com.epam.parkingcards.web.controller.api.BrandController;
import com.epam.parkingcards.web.request.BrandCreateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BrandControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BrandDao brandDao;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    @InjectMocks
    private BrandController brandController;

    @Autowired
    @InjectMocks
    private BrandService brandService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(brandController)
                .setControllerAdvice(new ExceptionController())
                .build();
    }

    @Test
    @WithMockUser(username = "name", roles = {"admin"})
    void create_ShouldReturnBrandId_WhenInputDataIsValid() throws Exception {

        BrandCreateRequest brand = new BrandCreateRequest();
        brand.setName("Testbrand");

        BrandEntity brandToDb = new BrandEntity();
        brandToDb.setName("Testbrand");

        BrandEntity brandFromDb = new BrandEntity();
        brandFromDb.setName("Testbrand");
        brandFromDb.setId(1);

        Mockito.when(brandDao.saveAndFlush(brandToDb)).thenReturn(brandFromDb);

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(brand))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Success, brand id = 1"));
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_500_WhenNameNotValid() throws Exception {

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Test@@brand\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    void create_ShouldReturnStatus_400_WhenNameAlreadyExist() throws Exception {
    }

    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnStatus_400_WhenNameAlreadyExistAndDeleted() throws Exception {

        BrandCreateRequest brand = new BrandCreateRequest();
        brand.setName("Testbrand");

        Mockito.when(brandDao.getCountDeletedByName(brand.getName())).thenReturn(1L);

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Testbrand\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @WithMockUser(roles = "user")
    @Test
    void create_ShouldReturnStatus_500_WhenRoleIsUser() throws Exception {
        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Testbrand\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnBrandResponse_WhenInputId_IsValidAndExist() throws Exception {

        BrandEntity testBrand = new BrandEntity();
        testBrand.setName("Testbrand");
        testBrand.setId(1L);
        testBrand.setDeleted(false);

        Mockito.when(brandDao.findById(1L)).thenReturn(Optional.of(testBrand));

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
    void getById_ShouldReturnStatus_500_WhenInputId_NotValid() throws Exception {
    }

    @Test
    @WithMockUser(roles = "admin")
    void getAllBrands_ShouldReturnListOfResult_WhenDataExist() throws Exception {

        BrandEntity testBrand2 = new BrandEntity();
        testBrand2.setId(1);
        testBrand2.setName("Testbrand");
        testBrand2.setDeleted(false);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Mockito.when(brandDao.findByIsDeletedFalse(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(testBrand2)));

        mockMvc.perform(get("/api/brands/page/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Testbrand")))
                .andExpect(jsonPath("$[0].deleted", is(false)));
        //   verify(brandDao, times(1)).findByIsDeletedFalse(pageable);
        //      .andExpect(content().json("[{\"name\" : \"Testbrand\" , \"id\" : 1 , \"deleted\" : false}]"));
    }

    void getAllActive_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {
    }

    void getAllDeleted_ShouldReturnListOfResult_WhenDataExist() throws Exception {
    }

    void getAllDeleted_ShouldReturnStatus_204_WhenDataNotExist() throws Exception {
    }

    void searchByPart_ShouldReturnListOfResult_WhenDataExist() throws Exception {
    }


    void searchByPart_ShouldReturnEmptyList_WhenDataNotExist() throws Exception {
    }

    @Test
    @WithMockUser(username = "name", roles = {"admin"})
    void update_ShouldReturnResponse_WhenDataIsValid() throws Exception {

        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1L);
        testBrand.setName("Testbrand");
        testBrand.setDeleted(false);

        Mockito.when(brandDao.findById(1L)).thenReturn(Optional.of(testBrand));
        Mockito.when(brandDao.getCountDeletedByName("Testbrand")).thenReturn(0L);
        Mockito.when(brandDao.saveAndFlush(testBrand)).thenReturn(testBrand);

        mockMvc.perform(put("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Testbrand\" , \"id\" : 1 , \"deleted\" : false}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\" : \"Testbrand\" , \"id\" : 1 , \"deleted\" : false}"));
    }

    void update_ShouldReturnStatus_500_WhenRoleIsUser() throws Exception {
    }

    void update_ShouldReturnStatus_400_WhenNameAlreadyExistAndDeleted() throws Exception {
    }

    void update_ShouldReturnStatus_500_WhenNameNotValid() throws Exception {
    }

    void delete_ShouldReturnStatus_200_WhenBrandWasDeleted() throws Exception {
    }

    void delete_ShouldReturnStatus_500_WhenBrandNotExist() throws Exception {
    }

    void delete_ShouldReturnStatus_500_WhenBrandAlreadyDeleted() throws Exception {
    }
}