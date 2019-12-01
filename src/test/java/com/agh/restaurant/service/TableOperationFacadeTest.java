package com.agh.restaurant.service;

import com.agh.restaurant.config.TestConfiguration;
import com.agh.restaurant.domain.dao.OrderRepository;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.service.impl.TableOperationFacadeImpl;
import com.google.api.client.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Array;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest
public class TableOperationFacadeTest {

    @MockBean
    TableRepository tableRepository;

//    @MockBean UserRepository userRepository;

    @MockBean ReservationRepository reservationRepository;

    @MockBean OrderRepository orderRepository;

    @InjectMocks
    TableOperationFacadeImpl tableOperationFacade;


    public void init(){
//        when(tableRepository.findOne()).
    }

    @Test
    void cannotCreateReservationWhenNoTablesInDatabase(){
        //given
        when(Lists.newArrayList(tableRepository.findAll())).thenReturn(new ArrayList<>());

        //when
        List<TableEntity> list = tableOperationFacade.getTableFreeAtCertainTime(LocalDateTime.now());

        //then
        assertThat(list).isEmpty();
    }

    @Test
    void cannotCreateReservationWhenNoTablesFreeAtCertainTimeInDatabase(){
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2000, Month.APRIL,1,0,0);

        when(Lists.newArrayList(tableRepository.findAll())).thenReturn(
                new ArrayList<>(Collections.singletonList(new TableEntity().withId((long) 1))));
        when(reservationRepository.getByTimeOfReservationEquals(localDateTime)).thenReturn(
                Collections.singletonList(new ReservationEntity().withTable(new TableEntity().withId((long) 1))));
        //when
        List<TableEntity> list = tableOperationFacade.getTableFreeAtCertainTime(localDateTime);

        //then
        assertThat(list).isEmpty();
    }

    @Test
    void createReservationWhenTableFreeAtCertainTimeInDatabase(){
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2000, Month.APRIL,1,0,0);

        when(Lists.newArrayList(tableRepository.findAll())).thenReturn(
                new ArrayList<>(Collections.singletonList(new TableEntity())));
        when(reservationRepository.getByTimeOfReservationEquals(localDateTime)).thenReturn(
               new ArrayList<>());
        //when
        List<TableEntity> list = tableOperationFacade.getTableFreeAtCertainTime(LocalDateTime.now());

        //then
        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(1);
    }



}