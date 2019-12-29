package com.agh.restaurant.service;

import com.agh.restaurant.domain.TableResponse;
import com.agh.restaurant.domain.dao.OrderRepository;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.impl.TableOperationFacadeImpl;
import com.google.api.client.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TableOperationFacadeTest {

    @MockBean
    TableRepository tableRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ReservationRepository reservationRepository;

    @MockBean
    OrderRepository orderRepository;

    @InjectMocks
    TableOperationFacadeImpl tableOperationFacade;

    @Test
    void shouldAddCorrectlyTable() {
        //when
        when(tableRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        //when
        TableEntity result = tableOperationFacade.createTable();

        //then
        assertThat(result).isNotNull();
    }

    @Test
    void shouldReturnEmptyList() {
        //when
        when(tableRepository.findAll()).thenReturn(new ArrayList<>());

        //when
        List<TableResponse> arrayList = tableOperationFacade.getAllTables();

        //then
        assertThat(arrayList).isEmpty();
    }

    @Test
    void cannotCreateReservationWhenNoTablesInDatabase() {
        //given
        when(Lists.newArrayList(tableRepository.findAll())).thenReturn(new ArrayList<>());

        //when
        List<TableEntity> list = tableOperationFacade.getTableFreeAtCertainTime(LocalDateTime.now(), null);

        //then
        assertThat(list).isEmpty();
    }

    @Test
    void cannotCreateReservationWhenNoTablesFreeAtCertainTimeInDatabase() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2000, Month.APRIL, 1, 0, 0);

        TableEntity tableEntity = new TableEntity().withId(1L);

        when(Lists.newArrayList(tableRepository.findAll())).thenReturn(
                new ArrayList<>(Collections.singletonList(tableEntity)));

        when(Lists.newArrayList(reservationRepository.findAll())).thenReturn(
                new ArrayList<>(Collections.singletonList(new ReservationEntity().withTable(tableEntity).withDuration(1).withDate(localDateTime))));
        //when
        List<TableEntity> list = tableOperationFacade.getTableFreeAtCertainTime(localDateTime, 1);

        //then
        assertThat(list).isEmpty();
    }

    @Test
    void createReservationWhenTableFreeAtCertainTimeInDatabase() {
        //given
        LocalDateTime localDateTime = LocalDateTime.of(2000, Month.APRIL, 1, 0, 0);

        TableEntity tableEntity = new TableEntity().withId(1L);

        when(Lists.newArrayList(tableRepository.findAll())).thenReturn(
                new ArrayList<>(Collections.singletonList(tableEntity)));

        when(Lists.newArrayList(reservationRepository.findAll())).thenReturn(
                new ArrayList<>(Collections.singletonList(new ReservationEntity().withTable(tableEntity).withDuration(1).withDate(localDateTime))));

        //when
        List<TableEntity> list = tableOperationFacade.getTableFreeAtCertainTime(LocalDateTime.now(), 1);

        //then
        assertThat(list).isNotEmpty();
        assertThat(list).hasSize(1);
    }

    @Test
    void checkIfCanSuccessfullyAssignWaiterToReservation() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubWaiter = new UserEntity();
        stubWaiter.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubWaiter);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)));

        //when
        ReservationEntity reservationEntity = tableOperationFacade.assignReservation(resId, "test", "waiter");

        //then
        assertThat(reservationEntity.getOrderEntity().getWaiter()).isEqualTo(stubWaiter);
        assertThat(reservationEntity.getTableReservation()).isEqualTo(stubTable);

    }

    @Test
    void checkIfCanSuccessfullyAssignBartenderToReservation() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubBartender = new UserEntity();
        stubBartender.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubBartender);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)));

        //when
        ReservationEntity reservationEntity = tableOperationFacade.assignReservation(resId, "test", "bartender");

        //then
        assertThat(reservationEntity.getOrderEntity().getBartender()).isEqualTo(stubBartender);
        assertThat(reservationEntity.getTableReservation()).isEqualTo(stubTable);

    }

    @Test
    void checkIfCanSuccessfullyAssignChefToReservation() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubChef = new UserEntity();
        stubChef.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubChef);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)));

        //when
        ReservationEntity reservationEntity = tableOperationFacade.assignReservation(resId, "test", "chef");

        //then
        assertThat(reservationEntity.getOrderEntity().getChef()).isEqualTo(stubChef);
        assertThat(reservationEntity.getTableReservation()).isEqualTo(stubTable);

    }

    @Test
    void checkIfCanSuccessfullyUnassignWaiterToReservation() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubWaiter = new UserEntity();
        stubWaiter.setUsername("test");

        OrderEntity orderEntity = new OrderEntity().withWaiter(stubWaiter).withId(1L);

        when(userRepository.findByUsername("test")).thenReturn(stubWaiter);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)
                        .withOrderEntity(orderEntity)));

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderEntity));

        //when
        ReservationEntity reservationEntity = tableOperationFacade.deleteReservation(resId, "test", "waiter");

        //then
        assertThat(reservationEntity.getOrderEntity()).isNotNull();
        assertThat(reservationEntity.getOrderEntity().getWaiter()).isNull();
    }

    @Test
    void checkIfCanSuccessfullyUnssignBartenderToReservation() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubBartender = new UserEntity();
        stubBartender.setUsername("test");

        OrderEntity orderEntity = new OrderEntity().withWaiter(stubBartender).withId(1L);

        when(userRepository.findByUsername("test")).thenReturn(stubBartender);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)
                        .withOrderEntity(orderEntity)));

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderEntity));

        //when
        ReservationEntity reservationEntity = tableOperationFacade.deleteReservation(resId, "test", "bartender");

        //then
        assertThat(reservationEntity.getOrderEntity()).isNotNull();
        assertThat(reservationEntity.getOrderEntity().getBartender()).isNull();

    }

    @Test
    void checkIfCanSuccessfullyUnassignChefToReservation() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubChef = new UserEntity();
        stubChef.setUsername("test");

        OrderEntity orderEntity = new OrderEntity().withWaiter(stubChef).withId(1L);

        when(userRepository.findByUsername("test")).thenReturn(stubChef);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)
                        .withOrderEntity(orderEntity)));

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderEntity));

        //when
        ReservationEntity reservationEntity = tableOperationFacade.deleteReservation(resId, "test", "chef");

        //then
        assertThat(reservationEntity.getOrderEntity()).isNotNull();
        assertThat(reservationEntity.getOrderEntity().getChef()).isNull();

    }

    @Test
    void checkIfUnassignWaiterToEmptyReservationFails() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubWaiter = new UserEntity();
        stubWaiter.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubWaiter);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)));

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tableOperationFacade.deleteReservation(resId, "test", "waiter"));

        //then
        assertThat(exception.getMessage()).isEqualTo("Reservation has no waiter assigned or is not assigned to you.");
    }

}