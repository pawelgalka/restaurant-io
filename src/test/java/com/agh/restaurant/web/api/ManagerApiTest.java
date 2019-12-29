package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        //when
        MvcResult mvcResult = mockMvc.perform(post(API_PREFIX + "/signup").contentType("application/json")
                .content(objectMapper.writeValueAsString(registerUserInit))).andExpect(status().is5xxServerError())
                .andReturn();

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
