package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.dao.ProductRepository;
import com.agh.restaurant.domain.model.ProductEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("it")
@PropertySource("classpath:./application-it.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "TEST_SUPPLIER", roles = { "SUPPLIER" }, password = "12345678")
class SupplierApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired ProductRepository productRepository;

    private Gson jsonParser = new Gson();

    private final String API_PREFIX = "/api/supplier";

    @Test
    void shouldAlterProductCorrectly() throws Exception {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setUsedInFoods(new ArrayList<>());
        productEntity.setAmount(10);
        productEntity.setName("test");
        productRepository.save(productEntity);

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/supply")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(Collections.singletonList(new ProductItem("test", 10)))))
                .andExpect(status().isOk()).andReturn();

        List<ProductEntity> productEntityList = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<ProductEntity>>() {
                }.getType());

        assertThat(productEntityList).hasSize(1);
        assertThat(productEntityList.get(0).getAmount()).isEqualTo(20);
    }

    @Test
    void shouldNotAlterProductIfNegativeAmount() throws Exception {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setUsedInFoods(new ArrayList<>());
        productEntity.setAmount(10);
        productEntity.setName("test");
        productRepository.save(productEntity);

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/supply")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(Collections.singletonList(new ProductItem("test", -10)))))
                .andExpect(status().is4xxClientError()).andReturn();

        assertTrue(true); //sonar requirement
    }

    @Test
    void shouldReturnEmptyList() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/requestedItems"))
                .andExpect(status().isOk()).andReturn();

        List<String> productEntityList = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<String>>() {
                }.getType());

        assertThat(productEntityList).hasSize(0);
    }

    @Test
    void shouldReturnRequiredProducts() throws Exception {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setUsedInFoods(new ArrayList<>());
        productEntity.setAmount(5);
        productEntity.setName("test");
        productRepository.save(productEntity);

        //when
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/requestedItems"))
                .andExpect(status().isOk()).andReturn();

        List<String> productEntityList = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<String>>() {
                }.getType());

        assertThat(productEntityList).hasSize(1);
        assertThat(productEntityList.get(0)).isEqualTo("test");
    }

    @Test
    void shouldReturnNoneProducts() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/products"))
                .andExpect(status().isOk()).andReturn();

        List<ProductEntity> productEntityList = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<ProductEntity>>() {
                }.getType());

        assertThat(productEntityList).isEmpty();
    }

    @Test
    void shouldReturnOneProducts() throws Exception {
        //given
        ProductEntity productEntity = new ProductEntity();
        productEntity.setUsedInFoods(new ArrayList<>());
        productEntity.setAmount(5);
        productEntity.setName("test");
        productRepository.save(productEntity);

        //when
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/products"))
                .andExpect(status().isOk()).andReturn();

        List<ProductEntity> productEntityList = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<ProductEntity>>() {
                }.getType());

        assertThat(productEntityList).hasSize(1);
        assertThat(productEntityList.get(0).getName()).isEqualTo("test");
    }
}