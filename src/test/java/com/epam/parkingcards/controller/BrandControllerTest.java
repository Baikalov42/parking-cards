package com.epam.parkingcards.controller;

import com.epam.parkingcards.dao.BrandDao;
import com.epam.parkingcards.exception.NotFoundException;
import com.epam.parkingcards.model.BrandEntity;
import com.epam.parkingcards.web.controller.api.BrandController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class BrandControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private BrandDao brandDao;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
//     @InjectMocks
    private BrandController brandController;

//    @Autowired
//    @InjectMocks
//    private BrandService brandService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(brandController)
                .build();
    }

    @Test
    @WithMockUser(username = "name", roles = {"admin"})
    void create_ShouldReturnBrandId_WhenInputDataIsValid() throws Exception {

        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1);
        testBrand.setName("Testbrand");

        Mockito.when(brandDao.saveAndFlush(any())).thenReturn(testBrand);
        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testBrand))
                //           .content("{\"name\" : \"Testbrand\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Success, brand id = 1"));
    }

    //todo not correct test         400ERROR
    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldThrowException_WhenNameAlreadyExist() throws Exception {
        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1);
        testBrand.setName("Testbrand");
        Mockito.when(brandDao.getCountDeletedByName(testBrand.getName())).thenReturn(1L);
        try {
            mockMvc.perform(post("/api/brands")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\" : \"Testbrand\"}")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } catch (NestedServletException e) {
            throw (Exception) e.getCause();
        }
    }


    @Test
    @WithMockUser(roles = "admin")
    void create_ShouldReturnError_WhenInputDataNotValid() throws Exception {

        mockMvc.perform(post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"Test@@brand\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //todo not correct test          403 ERROR
    @WithMockUser(roles = "user")
    @Test
    void create_By_User() throws Exception {
        mockMvc.perform(post("/api/brands")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"name\" : \"Testbrand\"}")
                .accept(MediaType.ALL))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "admin")
    void getById_ShouldReturnBrandResponse_WhenInputUrlId_IsValid() throws Exception {
        BrandEntity testBrand = new BrandEntity();
        testBrand.setName("Testbrand");
        testBrand.setId(1L);
        testBrand.setDeleted(false);
        Mockito.when(brandDao.findById(Mockito.anyLong())).thenReturn(Optional.of(testBrand));

        mockMvc.perform(get("/api/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Testbrand")))
                .andExpect(jsonPath("$.deleted", is(false)));

        //              .andExpect(content().json("{\"name\" : \"Testbrand\" , \"id\" : 1 , \"deleted\" : false}"));
    }

    //TODO wrong Id
    @Test
    @WithMockUser(roles = "admin")
    void getById_WhenInputUrlId_IsNotExist() throws Exception {

        Mockito.when(brandDao.findById(66L)).thenThrow(new NotFoundException("By id 66, Car not found"));

        mockMvc.perform(get("/api/brands/66"))
                .andExpect(status().isNoContent());

    }



    @Test
    @WithMockUser(roles = "admin")
    void getAllBrands_MarkNotDeleted() throws Exception {

        BrandEntity testBrand2 = new BrandEntity();
        testBrand2.setId(1);
        testBrand2.setName("Testbrand");
        testBrand2.setDeleted(false);
        //    testBrand2.setModelEntities(Collections.EMPTY_SET);

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        //Page<BrandEntity> foundPage = new PageImpl<>(brands);
        Mockito.when(brandDao.findByIsDeletedFalse(pageable)).thenReturn(new PageImpl<BrandEntity>(Arrays.asList(testBrand2)));

        mockMvc.perform(get("/api/brands/page/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Testbrand")))
                .andExpect(jsonPath("$[0].deleted", is(false)));
        //   verify(brandDao, times(1)).findByIsDeletedFalse(pageable);
        //      .andExpect(content().json("[{\"name\" : \"Testbrand\" , \"id\" : 1 , \"deleted\" : false}]"));


    }

    @Test
    @WithMockUser(username = "name", roles = {"admin"})
    void updateBrand_ValidInfo() throws Exception {

        BrandEntity testBrand = new BrandEntity();
        testBrand.setId(1L);
        testBrand.setName("Testbrand");
        testBrand.setDeleted(false);

        BrandEntity testBrand2 = new BrandEntity();
        testBrand2.setId(1L);
        testBrand2.setName("TestTestbrand");
        testBrand2.setDeleted(false);

        Mockito.when(brandDao.findById(Mockito.anyLong())).thenReturn(Optional.of(testBrand));
        Mockito.when(brandDao.getCountDeletedByName(anyString())).thenReturn(0L);
        Mockito.when(brandDao.saveAndFlush(any())).thenReturn(testBrand2);

        mockMvc.perform(put("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"TestTestbrand\" , \"id\" : 1 , \"deleted\" : false}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\" : \"TestTestbrand\" , \"id\" : 1 , \"deleted\" : false}"));
    }


}

//findByIsDeletedFalse
//   .andExpect(content().json("{\"name\" : \"TestTestbrand\" , \"id\" : 1 , \"deleted\" : false}"));