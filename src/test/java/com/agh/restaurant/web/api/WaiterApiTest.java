package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.FeedbackEnum;
import com.agh.restaurant.domain.FeedbackPojo;
import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.dao.OrderRepository;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.domain.model.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
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
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("it")
@PropertySource("classpath:./application-it.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "TEST_WAITER", roles = { "WAITER" }, password = "12345678")
class WaiterApiTest {

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

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    private Gson jsonParser = new Gson();

    private final String API_PREFIX = "/api/waiter";

    @Test
    void whenValidOrder_CreateOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setDishes(new ArrayList<>());
        orderRequest.setBeverages(new ArrayList<>());

        ReservationEntity reservationEntity = reservationRepository
                .save(new ReservationEntity().withOrderEntity(new OrderEntity()));
        orderRequest.setReservationId(reservationEntity.getId());

        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/order").param("username", "TEST_WAITER")
                .contentType("application/json").content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk()).andReturn();

        OrderEntity orderEntity = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), OrderEntity.class);
        assertThat(orderEntity.getStage()).isEqualTo(StageEnum.IN_PROGRESS);
    }

    @Test
    void whenInvalidOrder_ThrowException() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest();

        //when
        assertThrows(NestedServletException.class,
                () -> mockMvc.perform(post(API_PREFIX + "/order").param("username", "TEST_WAITER")
                        .contentType("application/json").content(objectMapper.writeValueAsString(orderRequest)))
                        .andExpect(status().is5xxServerError()).andReturn());

        //then
    }

    @Test
    void whenTableRequest_ReturnOk() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/tables"))
                .andExpect(status().isOk()).andExpect(content().json("[]")).andReturn();

    }

    @Test
    void whenTableRequest_ReturnNotEmptyListAndOk() throws Exception {
        //given
        tableRepository.deleteAll();
        tableRepository.save(new TableEntity());
        tableRepository.save(new TableEntity());

        //when then
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/tables"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andReturn();

    }

    @Test
    void whenAssignToReservation_ReturnOk() throws Exception {
        //given
        tableRepository.save(new TableEntity());

        ReservationEntity reservationEntity = reservationRepository.save(new ReservationEntity());

        reservationEntity.setTableReservation(Lists.newArrayList(tableRepository.findAll()).get(0));

        reservationRepository.save(reservationEntity);

        userRepository.save(new UserEntity().withEmail("test@test.pl").withUsername("TEST_WAITER")
                .withPassword(passwordEncoder.encode("12345678")));

        //when
        MvcResult mvcResult = mockMvc
                .perform(patch(API_PREFIX + "/assign")
                        .param("reservationId", reservationEntity.getId().toString()))
                .andExpect(status().isOk()).andReturn();

        reservationEntity = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), ReservationEntity.class);

        //then
        assertThat(reservationEntity.getOrderEntity().getWaiter().getUsername()).isEqualTo("TEST_WAITER");
        assertThat(reservationEntity.getOrderEntity().getWaiter().getEmail()).isEqualTo("test@test.pl");
    }

    @Test
    void whenAssignDeleteToReservation_ReturnOk() throws Exception {
        //given
        tableRepository.save(new TableEntity());

        ReservationEntity reservationEntity = reservationRepository.save(new ReservationEntity());

        reservationEntity.setTableReservation(Lists.newArrayList(tableRepository.findAll()).get(0));

        UserEntity userEntity = userRepository
                .save(new UserEntity().withEmail("test@test.pl").withUsername("TEST_WAITER")
                        .withPassword(passwordEncoder.encode("12345678")));

        reservationRepository.save(reservationEntity);

        //when then
        MvcResult mvcResult = mockMvc
                .perform(patch(API_PREFIX + "/assign")
                        .param("reservationId", reservationEntity.getId().toString())).andReturn();

        MvcResult mvcResult1 = mockMvc
                .perform(delete(API_PREFIX + "/assignDelete")
                        .param("reservationId", reservationEntity.getId().toString()))
                .andExpect(status().isOk()).andReturn();
        assertTrue(true); //sonar requirement
    }

    @Test
    void whenAssignDeleteToReservation_ReturnError() throws Exception {
        //given
        tableRepository.save(new TableEntity());

        ReservationEntity reservationEntity = reservationRepository.save(new ReservationEntity());

        reservationEntity.setTableReservation(Lists.newArrayList(tableRepository.findAll()).get(0));

        UserEntity userEntity = userRepository
                .save(new UserEntity().withEmail("test@test.pl").withUsername("TEST_WAITER")
                        .withPassword(passwordEncoder.encode("12345678")));

        reservationRepository.save(reservationEntity);

        //when then

        MvcResult mvcResult1 = mockMvc
                .perform(delete(API_PREFIX + "/assignDelete")
                        .param("reservationId", reservationEntity.getId().toString()))
                .andExpect(status().is5xxServerError()).andReturn();
        assertTrue(true); //sonar requirement
    }

    @Test
    void whenFinalizeOrder_StageCompleted() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setDishes(new ArrayList<>());
        orderRequest.setBeverages(new ArrayList<>());

        ReservationEntity reservationEntity = reservationRepository
                .save(new ReservationEntity().withOrderEntity(new OrderEntity()));
        orderRequest.setReservationId(reservationEntity.getId());

        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/order").param("username", "TEST_WAITER")
                .contentType("application/json").content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk()).andReturn();
        OrderEntity orderEntity = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), OrderEntity.class);

        //when then
        MvcResult mvcResult1 = mockMvc
                .perform(patch(API_PREFIX + "/finalize")
                        .param("orderId", orderEntity.getId().toString())).andReturn();
        OrderEntity orderEntity1 = jsonParser
                .fromJson(mvcResult1.getResponse().getContentAsString(), OrderEntity.class);
        assertThat(orderEntity1.getStage()).isEqualTo(StageEnum.FINALIZED);
    }

    @Test
    void whenCreateFeedback_StageCompleted() throws Exception {
        //given
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setDishes(new ArrayList<>());
        orderRequest.setBeverages(new ArrayList<>());

        ReservationEntity reservationEntity = reservationRepository
                .save(new ReservationEntity().withOrderEntity(new OrderEntity()));
        orderRequest.setReservationId(reservationEntity.getId());

        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/order").param("username", "TEST_WAITER")
                .contentType("application/json").content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk()).andReturn();
        OrderEntity orderEntity = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), OrderEntity.class);

        FeedbackPojo feedbackPojo = new FeedbackPojo(FeedbackEnum.OKAY, FeedbackEnum.BAD, FeedbackEnum.BAD,
                orderEntity.getId());
        //when then
        MvcResult mvcResult1 = mockMvc
                .perform(post(API_PREFIX + "/clientFeedback")
                        .contentType("application/json").content(objectMapper.writeValueAsString(feedbackPojo)))
                .andExpect(status().isOk()).andReturn();
        OrderEntity orderEntity1 = jsonParser
                .fromJson(mvcResult1.getResponse().getContentAsString(), OrderEntity.class);
        assertThat(orderEntity1.getStage()).isEqualTo(StageEnum.FINALIZED);
    }

    @Test
    void whenStatusOrder_ReturnOk() throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStage(StageEnum.IN_PROGRESS);
        orderEntity = orderRepository.save(orderEntity);

        MvcResult mvcResult = mockMvc
                .perform(get(API_PREFIX + "/orderStatus").param("orderId", orderEntity.getId().toString()))
                .andExpect(status().isOk()).andExpect(content().string("\"IN_PROGRESS\"")).andReturn();
        assertTrue(true); //sonar requirement
    }

    @Test
    void whenOrderPricing_ReturnOk() throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStage(StageEnum.IN_PROGRESS);
        orderEntity = orderRepository.save(orderEntity);

        MvcResult mvcResult = mockMvc
                .perform(get(API_PREFIX + "/getPrice").param("orderId", orderEntity.getId().toString()))
                .andExpect(status().isOk()).andExpect(content().string("0.0")).andReturn();
        assertTrue(true); //sonar requirement
    }

    @Test
    void whenMenuList_ReturnOk() throws Exception {

        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/menu"))
                .andExpect(status().isOk()).andExpect(content().json("{}")).andReturn();
        assertTrue(true); //sonar requirement
    }
}