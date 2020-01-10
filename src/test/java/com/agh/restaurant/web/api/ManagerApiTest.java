package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.FoodResponse;
import com.agh.restaurant.domain.ProductItem;
import com.agh.restaurant.domain.RestaurantMenuItem;
import com.agh.restaurant.domain.dao.FoodRepository;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.ProductEntity;
import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("it")
@PropertySource("classpath:./application-it.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "TEST_MANAGER", roles = { "MANAGER" }, password = "12345678")
class ManagerApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired FoodRepository foodRepository;

    private Gson jsonParser = new Gson();

    private final String API_PREFIX = "/api/management";

    @Test
    void createUser_ReturnsOk() throws Exception {
        //given
        RegisterUserInit registerUserInit = createTestUser();

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/signup").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andExpect(status().isOk())
                .andReturn();

        //then
        assertThat(userRepository.findByUsername("test")).isNotNull();
        assertThat(userRepository.findByUsername("test").getEmail()).isEqualTo("test@test.pl");
    }

    @Test
    void createUser_ReturnsErrorLackOfUsername() throws Exception {
        //given
        RegisterUserInit registerUserInit = new RegisterUserInit();
        registerUserInit.setPassword("123456");
        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/signup").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andExpect(status().is5xxServerError())
                .andReturn();

    }

    @Test
    void createUser_ReturnsErrorLackOfPassword() throws Exception {
        //given
        RegisterUserInit registerUserInit = new RegisterUserInit();
        registerUserInit.setUsername("test");
        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/signup").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andExpect(status().is5xxServerError())
                .andReturn();

    }

    @Test
    void updateUser_ReturnsOk() throws Exception {
        //given
        RegisterUserInit registerUserInit = createTestUser();
        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/signup").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andReturn();

        final String email = "update@mail.pl";

        registerUserInit.setEmail(email);

        MvcResult mvcResult1 = mockMvc.perform(patch(API_PREFIX + "/update").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andExpect(status().isOk())
                .andReturn();

        assertThat(userRepository.findByUsername("test").getEmail()).isEqualTo(email);
    }

    @Test
    void updateUser_ReturnsErrorDueToMissingUser() throws Exception {
        //given
        RegisterUserInit registerUserInit = createTestUser();

        //when

        MvcResult mvcResult1 = mockMvc.perform(patch(API_PREFIX + "/update").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andExpect(status().is5xxServerError())
                .andReturn();
    }

    @Test
    void deleteUserById_ReturnsOk() throws Exception {
        //given
        RegisterUserInit registerUserInit = createTestUser();

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/signup").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andReturn();

        UserEntity userEntity = userRepository.findByUsername("test");

        MvcResult mvcResult1 = mockMvc.perform(delete(API_PREFIX + "/deleteUserId/" + userEntity.getId()))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(userRepository.findByUsername("test")).isNull();
    }

    @Test
    void deleteUserById_ReturnsErrorDueToMissingUser() throws Exception {

        //when

        MvcResult mvcResult1 = mockMvc.perform(delete(API_PREFIX + "/deleteUserId/-1"))
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    @Test
    void deleteUserByUsername_ReturnsOk() throws Exception {
        //given
        RegisterUserInit registerUserInit = createTestUser();

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/signup").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andReturn();

        UserEntity userEntity = userRepository.findByUsername("test");

        MvcResult mvcResult1 = mockMvc.perform(delete(API_PREFIX + "/deleteUserName/" + userEntity.getUsername()))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(userRepository.findByUsername("test")).isNull();
    }

    @Test
    void deleteUserByUsername_ReturnsErrorDueToMissingUser() throws Exception {

        //when

        MvcResult mvcResult1 = mockMvc.perform(delete(API_PREFIX + "/deleteUserName/qwerty"))
                .andExpect(status().is5xxServerError())
                .andReturn();
    }

    @Test
    void fetchUsers_ReturnsValidNumberPositive() throws Exception {
        //given
        RegisterUserInit registerUserInit = createTestUser();

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/signup").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit)))
                .andReturn();

        MvcResult mvcResult1 = mockMvc.perform(get(API_PREFIX + "/fetchUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();

    }

    @Test
    void fetchUsers_ReturnsOnlyAdmin() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/fetchUsers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
    }

    @Test
    void addTable_AddTableCorrectly() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/addTable"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(Lists.newArrayList(tableRepository.findAll())).hasSize(1);
    }

    @Test
    void addTableCustomNumberOfTables_AddTablesCorrectly() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/addTables/10"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(Lists.newArrayList(tableRepository.findAll())).hasSize(10);
    }

    @Test
    void addTableNegativeNumberOfTables_AddNoTables() throws Exception {
        //given

        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/addTables/-1"))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(Lists.newArrayList(tableRepository.findAll())).hasSize(0);
    }

    @Test
    void addMenuItemCorrectly() throws Exception {
        //given
        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setItemsNeededNames(Collections.EMPTY_LIST);
        restaurantMenuItem.setName("test");

        //when
        MvcResult mvcResult = mockMvc
                .perform(post(API_PREFIX + "/addMenuItem").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantMenuItem)))
                .andExpect(status().isOk())
                .andReturn();

        FoodEntity foodEntity = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), FoodEntity.class);

        assertThat(foodEntity.getName()).isEqualTo("test");
    }

    @Test
    void addNotValidMenuItemFails() throws Exception {
        //given
        RestaurantMenuItem restaurantMenuItem = new RestaurantMenuItem();
        restaurantMenuItem.setItemsNeededNames(null);
        restaurantMenuItem.setName("test");

        //when
        MvcResult mvcResult = mockMvc
                .perform(post(API_PREFIX + "/addMenuItem").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantMenuItem)))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    void deleteItemCorrectly() throws Exception {
        //given

        FoodEntity foodEntity = foodRepository.save(new FoodEntity());

        //when
        MvcResult mvcResult1 = mockMvc
                .perform(delete(API_PREFIX + "/deleteMenuItem/" + foodEntity.getId()))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void addProductItemCorrectly() throws Exception {
        //given
        ProductItem productItem = new ProductItem();
        productItem.setName("test");
        productItem.setAmount(0);

        //when
        MvcResult mvcResult = mockMvc
                .perform(post(API_PREFIX + "/addProductItem").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productItem)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    void returnEmptyMenuList() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(get(API_PREFIX + "/getMenu"))
                .andExpect(status().isOk())
                .andReturn();

        Map<FoodEntity.FoodType, List<FoodResponse>> menu = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(),
                        new TypeToken<Map<FoodEntity.FoodType, List<FoodResponse>>>() {
                        }.getType());

        assertThat(menu.size()).isEqualTo(0);
    }

    @Test
    void returnEmptyProductList() throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(get(API_PREFIX + "/products"))
                .andExpect(status().isOk())
                .andReturn();

        List<ProductEntity> menu = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<ProductEntity>>() {
                }.getType());

        assertThat(menu.size()).isEqualTo(0);
    }

    private RegisterUserInit createTestUser() {
        RegisterUserInit registerUserInit = new RegisterUserInit();
        registerUserInit.setUsername("test");
        registerUserInit.setEmail("test@test.pl");
        registerUserInit.setRole("ROLE_MANAGER");
        registerUserInit.setPassword("12345678");
        return registerUserInit;
    }
}
