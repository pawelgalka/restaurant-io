package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.domain.model.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "TEST_WAITER", roles = { "WAITER" })
class WaiterApiTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean TableRepository tableRepository;

    @MockBean ReservationRepository reservationRepository;

    @MockBean UserRepository userRepository;

    private Gson jsonParser = new Gson();

    private final String API_PREFIX = "/api/waiter";

    @Test
    void whenValidOrder_CreateOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setReservationId(1L);
        orderRequest.setDishes(new ArrayList<>());
        orderRequest.setBeverages(new ArrayList<>());

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
        List<TableEntity> tableEntities = new ArrayList<>(
                Arrays.asList(new TableEntity().withId(1L).withTableReservations(new ArrayList<>()),
                        new TableEntity().withId(2L).withTableReservations(new ArrayList<>())));

        when(tableRepository.findAll()).thenReturn(tableEntities);

        //when then
        MvcResult mvcResult = mockMvc.perform(get(API_PREFIX + "/tables"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2))).andReturn();

    }

    @Test
    void whenAssignToReservation_ReturnOk() throws Exception {
        //given
        TableEntity tableEntity = new TableEntity().withId(1L).withTableReservations(new ArrayList<>());

        when(reservationRepository.findById(anyLong())).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(tableEntity)));

        when(userRepository.findByUsername(anyString())).thenReturn(new UserEntity().withEmail("test@test.pl").withUsername("TEST_WAITER"));

        when(reservationRepository.save(ArgumentMatchers.any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        //when
        MvcResult mvcResult = mockMvc
                .perform(patch(API_PREFIX + "/assign")
                        .requestAttr("username", "TEST_WAITER")
                        .param("reservationId", "1"))
                .andExpect(status().isOk()).andReturn();

        ReservationEntity reservationEntity = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), ReservationEntity.class);

        //then
        assertThat(reservationEntity.getOrderEntity().getWaiter().getUsername()).isEqualTo("TEST_WAITER");
        assertThat(reservationEntity.getOrderEntity().getWaiter().getEmail()).isEqualTo("test@test.pl");
    }
}