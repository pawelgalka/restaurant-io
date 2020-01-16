package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("it")
@PropertySource("classpath:./application-it.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CustomerApiTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    ReservationRepository reservationRepository;

    private Gson jsonParser = new Gson();

    private final String API_PREFIX = "/api/customer";

    private final String CUSTOMER_ENDPOINT = API_PREFIX + "/reserve";

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    private static LocalDateTime testDate = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        tableRepository.save(new TableEntity());
    }

    @Test
    void createRes_ReturnsOk() throws Exception {
        //given
        testDate = testDate.withHour(13).withMinute(0).withSecond(0);

        //when
        MvcResult mvcResult = mockMvc.perform(post(CUSTOMER_ENDPOINT).param("customerName", "TEST_CUSTOMER")
                .param("date", testDate.format(dateFormatter)).param("duration", "1")).andExpect(status().isOk())
                .andReturn();

        Long reservationId = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), Long.class);

        //then
        assertThat(reservationId).isPositive();
    }

    @Test
    void createRes_ReturnsErrorDueToLackOfTables() throws Exception {
        //given
        testDate = testDate.withHour(13).withMinute(0).withSecond(0);
        tableRepository.deleteAll();

        //when then
        MvcResult mvcResult = mockMvc.perform(post(CUSTOMER_ENDPOINT).param("customerName", "TEST_CUSTOMER")
                .param("date", testDate.format(dateFormatter)).param("duration", "1")).andExpect(status().isNotAcceptable())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("No available table at certain time. No changes made to reservation.");
    }

    @Test
    void createRes_ReturnsErrorWrongHour() throws Exception {
        //given
        testDate = testDate.withHour(4).withMinute(0).withSecond(0);

        //when then
        MvcResult mvcResult = mockMvc.perform(post(CUSTOMER_ENDPOINT).param("customerName", "TEST_CUSTOMER")
                .param("date", testDate.format(dateFormatter)).param("duration", "1")).andExpect(status().isNotAcceptable())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Restaurant opening hours exceeded. Please choose time slot between 12AM and 12PM.");
    }

    @Test
    void updateRes_ReturnsOk() throws Exception {
        //given
        testDate = testDate.withHour(13).withMinute(0).withSecond(0);
        ReservationEntity reservationEntity = reservationRepository.save(new ReservationEntity());

        //when
        MvcResult mvcResult = mockMvc.perform(patch(CUSTOMER_ENDPOINT).param("customerName", "TEST_CUSTOMER")
                .param("reservationId", String.valueOf(reservationEntity.getId()))
                .param("date", testDate.format(dateFormatter)).param("duration", "1")).andExpect(status().isOk())
                .andReturn();

        Long reservationId = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), Long.class);

        //then
        assertThat(reservationId).isEqualTo(reservationEntity.getId());
    }

    @Test
    void updateRes_ReturnsOkNoDurationCase() throws Exception {
        //given
        testDate = testDate.withHour(13).withMinute(0).withSecond(0);
        testDate = testDate.withHour(13).withMinute(0).withSecond(0);
        ReservationEntity basicReservationEntity = new ReservationEntity();
        basicReservationEntity.setDuration(1);
        ReservationEntity reservationEntity = reservationRepository.save(basicReservationEntity);

        //when
        MvcResult mvcResult = mockMvc.perform(patch(CUSTOMER_ENDPOINT).param("customerName", "TEST_CUSTOMER")
                .param("reservationId", String.valueOf(reservationEntity.getId()))
                .param("date", testDate.format(dateFormatter))).andExpect(status().isOk())
                .andReturn();

        Long reservationId = jsonParser.fromJson(mvcResult.getResponse().getContentAsString(), Long.class);

        //then
        assertThat(reservationId).isEqualTo(reservationEntity.getId());
    }

    @Test
    void updateRes_ReturnsErrorWrongHour() throws Exception {
        //given
        testDate = testDate.withHour(4).withMinute(0).withSecond(0);
        ReservationEntity reservationEntity = reservationRepository.save(new ReservationEntity());

        //when then
        MvcResult mvcResult = mockMvc.perform(post(CUSTOMER_ENDPOINT).param("customerName", "TEST_CUSTOMER")
                .param("reservationId", String.valueOf(reservationEntity.getId()))
                .param("date", testDate.format(dateFormatter)).param("duration", "1")).andExpect(status().isNotAcceptable())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Restaurant opening hours exceeded. Please choose time slot between 12AM and 12PM.");
        assertThat(reservationRepository.existsById(reservationEntity.getId())).isTrue();
    }

    @Test
    void updateRes_ReturnsErrorDueToLackOfTables() throws Exception {
        //given
        testDate = testDate.withHour(13).withMinute(0).withSecond(0);
        ReservationEntity reservationEntity = reservationRepository.save(new ReservationEntity().withDate(testDate).withDuration(1));
        tableRepository.deleteAll();

        //when then
        MvcResult mvcResult = mockMvc.perform(post(CUSTOMER_ENDPOINT).param("customerName", "TEST_CUSTOMER")
                .param("reservationId", String.valueOf(reservationEntity.getId()))
                .param("date", testDate.format(dateFormatter)).param("duration", "1")).andExpect(status().isNotAcceptable())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("No available table at certain time. No changes made to reservation.");
        assertThat(reservationRepository.existsById(reservationEntity.getId())).isTrue();
    }

    @Test
    void deleteRes_ReturnsOk() throws Exception {
        //given
        ReservationEntity reservationEntity = reservationRepository.save(new ReservationEntity());

        //when then
        MvcResult mvcResult = mockMvc.perform(delete(CUSTOMER_ENDPOINT)
                .param("reservationId", String.valueOf(reservationEntity.getId())))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("reservation deleted");
    }

    @Test
    void deleteRes_ReturnsError() throws Exception {
        //given

        //when then
        MvcResult mvcResult = mockMvc.perform(delete(CUSTOMER_ENDPOINT)
                .param("reservationId", String.valueOf(-1)))
                .andExpect(status().isInsufficientStorage())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("unable to delete reservation.");
    }

}
