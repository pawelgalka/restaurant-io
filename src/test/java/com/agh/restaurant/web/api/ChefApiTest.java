package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.FoodResponse;
import com.agh.restaurant.domain.OrderResponse;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.dao.OrderRepository;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.*;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("it")
@PropertySource("classpath:./application-it.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "TEST_COOK", roles = { "COOK" }, password = "12345678")
class ChefApiTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    private Gson jsonParser = new Gson();

    private final String API_PREFIX = "/api/cook";

    @Test
    void shouldReturnDishComplete() throws Exception {
        //given
        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setNeededProducts(Collections.emptyList());

        OrderEntity orderEntity = orderRepository.save(new OrderEntity().withStage(StageEnum.IN_PROGRESS).withDishes(Collections.EMPTY_LIST));
        System.out.println(orderEntity);
        //when then
        MvcResult mvcResult1 = mockMvc
                .perform(patch(API_PREFIX + "/changeState")
                        .param("orderId", orderEntity.getId().toString())).andReturn();
        OrderEntity orderEntity1 = jsonParser.fromJson(mvcResult1.getResponse().getContentAsString(), OrderEntity.class);
        assertThat(orderEntity1.getStage()).isEqualTo(StageEnum.DISH_COMPLETE);
    }

    @Test
    void shouldReturnAllComplete() throws Exception {
        //given
        FoodEntity foodEntity = new FoodEntity();
        foodEntity.setNeededProducts(Collections.emptyList());

        OrderEntity orderEntity = orderRepository.save(new OrderEntity().withStage(StageEnum.BEVERAGE_COMPLETE).withDishes(Collections.EMPTY_LIST));
        System.out.println(orderEntity);

        //when then
        MvcResult mvcResult1 = mockMvc
                .perform(patch(API_PREFIX + "/changeState")
                        .param("orderId", orderEntity.getId().toString())).andReturn();
        OrderEntity orderEntity1 = jsonParser.fromJson(mvcResult1.getResponse().getContentAsString(), OrderEntity.class);
        assertThat(orderEntity1.getStage()).isEqualTo(StageEnum.ALL_COMPLETE);
    }

    @Test
    void shouldReturnNotEmptyList() throws Exception {
        //given

        UserEntity userEntity = userRepository
                .save(new UserEntity().withEmail("test@test.pl").withUsername("TEST_WAITER")
                        .withPassword(passwordEncoder.encode("12345678")));

        orderRepository.save(new OrderEntity().withChef(userEntity).withDishes(
                Collections.singletonList(new FoodEntity())).withStage(StageEnum.IN_PROGRESS));


        //when
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/getDishOrders"))
                .andExpect(status().isOk()).andReturn();

        List<OrderResponse> productEntityList = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<OrderResponse>>() {
                }.getType());

        assertThat(productEntityList).hasSize(1);
        assertThat(productEntityList.get(0).getChef()).isEqualTo("TEST_WAITER");
    }

    @Test
    void shouldReturnCorrectList() throws Exception {
        //given

        UserEntity userEntity = userRepository
                .save(new UserEntity().withEmail("test@test.pl").withUsername("TEST_COOK")
                        .withPassword(passwordEncoder.encode("12345678")));

        orderRepository.save(new OrderEntity().withChef(userEntity).withDishes(
                Collections.singletonList(new FoodEntity())).withStage(StageEnum.IN_PROGRESS));


        //when
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/getDishOrders"))
                .andExpect(status().isOk()).andReturn();

        List<OrderResponse> productEntityList = jsonParser
                .fromJson(mvcResult.getResponse().getContentAsString(), new TypeToken<List<OrderResponse>>() {
                }.getType());

        assertThat(productEntityList).hasSize(1);
    }

    @Test
    void whenAssignToReservation_ReturnOk() throws Exception {
        //given

        userRepository.save(new UserEntity().withEmail("test@test.pl").withUsername("TEST_COOK")
                .withPassword(passwordEncoder.encode("12345678")));

        OrderEntity orderEntity = orderRepository.save(new OrderEntity());

        //when
        MvcResult mvcResult = mockMvc
                .perform(patch(API_PREFIX + "/assign")
                        .param("orderId", orderEntity.getId().toString()))
                .andExpect(status().isOk()).andReturn();

        OrderEntity orderEntity1 = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), OrderEntity.class);

        //then
        assertThat(orderEntity1.getChef().getUsername()).isEqualTo("TEST_COOK");
        assertThat(orderEntity1.getChef().getEmail()).isEqualTo("test@test.pl");
    }

    @Test
    void whenAssignDeleteToReservation_ReturnOk() throws Exception {
        //given

        UserEntity userEntity = userRepository
                .save(new UserEntity().withEmail("test@test.pl").withUsername("TEST_COOK")
                        .withPassword(passwordEncoder.encode("12345678")));

        OrderEntity orderEntity = orderRepository.save(new OrderEntity());

        //when then
        MvcResult mvcResult = mockMvc
                .perform(patch(API_PREFIX + "/assign")
                        .param("orderId", orderEntity.getId().toString())).andReturn();

        MvcResult mvcResult1 = mockMvc
                .perform(delete(API_PREFIX + "/assignDelete")
                        .param("orderId", orderEntity.getId().toString()))
                .andExpect(status().isOk()).andReturn();

    }

    @Test
    void whenAssignDeleteToReservation_ReturnError() throws Exception {
        //given

        UserEntity userEntity = userRepository
                .save(new UserEntity().withEmail("test@test.pl").withUsername("TEST_COOK")
                        .withPassword(passwordEncoder.encode("12345678")));

        OrderEntity orderEntity = orderRepository.save(new OrderEntity());

        //when then

        MvcResult mvcResult1 = mockMvc
                .perform(delete(API_PREFIX + "/assignDelete")
                        .param("orderId", orderEntity.getId().toString()))
                .andExpect(status().is5xxServerError()).andReturn();
    }
}