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
import com.google.common.collect.Lists;
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
                new ArrayList<>(Collections.singletonList(
                        new ReservationEntity().withTable(tableEntity).withDuration(1).withDate(localDateTime))));
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
                new ArrayList<>(Collections.singletonList(
                        new ReservationEntity().withTable(tableEntity).withDuration(1).withDate(localDateTime))));

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
        ReservationEntity reservationEntity = tableOperationFacade.assignReservation(resId, "test");

        //then
        assertThat(reservationEntity.getOrderEntity().getWaiter()).isEqualTo(stubWaiter);
        assertThat(reservationEntity.getTableReservation()).isEqualTo(stubTable);

    }

    @Test
    void checkIfCanSuccessfullyAssignWaiterToReservationOrderAlreadyExists() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubWaiter = new UserEntity();
        stubWaiter.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubWaiter);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional
                        .ofNullable(new ReservationEntity().withTable(stubTable).withOrderEntity(new OrderEntity())));

        //when
        ReservationEntity reservationEntity = tableOperationFacade.assignReservation(resId, "test");

        //then
        assertThat(reservationEntity.getOrderEntity().getWaiter()).isEqualTo(stubWaiter);
        assertThat(reservationEntity.getTableReservation()).isEqualTo(stubTable);

    }

    @Test
    void checkIfThrowIfAlreadyAssignWaiterToReservation() {
        //given
        Long tableId = 1L;
        Long resId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubWaiter = new UserEntity();
        stubWaiter.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubWaiter);

        when(reservationRepository.findById(resId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)
                        .withOrderEntity(new OrderEntity().withWaiter(stubWaiter))));

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tableOperationFacade.assignReservation(resId, "test"));

        //then
        assertThat(exception.getMessage()).isEqualTo("Something went wrong.");
    }

    @Test
    void checkIfCanSuccessfullyAssignBartenderToReservation() {
        //given
        Long orderId = 1L;
        UserEntity stubBartender = new UserEntity();
        stubBartender.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubBartender);

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.of(new OrderEntity()));

        //when
        OrderEntity orderEntity = tableOperationFacade.assignReservationKitchen(orderId, "test", "bartender");

        //then
        assertThat(orderEntity.getBartender()).isEqualTo(stubBartender);

    }

    @Test
    void checkIfThrowIfAlreadyAssignedBartenderToReservation() {
        //given
        Long orderId = 1L;
        UserEntity stubBartender = new UserEntity();
        stubBartender.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubBartender);

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.of(new OrderEntity().withBartender(stubBartender)));
        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tableOperationFacade.assignReservationKitchen(orderId, "test", "bartender"));

        //then
        assertThat(exception.getMessage()).isEqualTo("Something went wrong.");
    }

    @Test
    void checkIfCanSuccessfullyAssignChefToReservation() {
        //given
        Long orderId = 1L;
        UserEntity stubBartender = new UserEntity();
        stubBartender.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubBartender);

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.of(new OrderEntity()));

        //when
        OrderEntity orderEntity = tableOperationFacade.assignReservationKitchen(orderId, "test", "chef");

        //then
        assertThat(orderEntity.getChef()).isEqualTo(stubBartender);

    }

    @Test
    void checkIfThrowIfAlreadyAssignedChefToReservation() {
        //given
        Long orderId = 1L;
        UserEntity stubChef = new UserEntity();
        stubChef.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubChef);

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.of(new OrderEntity().withChef(stubChef)));
        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tableOperationFacade.assignReservationKitchen(orderId, "test", "chef"));

        //then
        assertThat(exception.getMessage()).isEqualTo("Something went wrong.");
    }

    @Test
    void checkIfThrowIfIncorrectType() {
        //given
        Long orderId = 1L;
        UserEntity stubChef = new UserEntity();
        stubChef.setUsername("test");

        when(userRepository.findByUsername("test")).thenReturn(stubChef);

        when(orderRepository.findById(orderId)).thenReturn(
                java.util.Optional.of(new OrderEntity().withChef(stubChef)));
        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tableOperationFacade.assignReservationKitchen(orderId, "test", "someJunk"));

        //then
        assertThat(exception.getMessage()).isEqualTo("Something went wrong.");
    }

    @Test
    void checkIfCanSuccessfullyUnassignWaiterToReservation() {
        //given
        Long tableId = 1L;
        Long orderId = 1L;
        TableEntity stubTable = new TableEntity().withId(tableId);
        UserEntity stubWaiter = new UserEntity();
        stubWaiter.setUsername("test");

        OrderEntity orderEntity = new OrderEntity().withWaiter(stubWaiter).withId(1L);

        when(userRepository.findByUsername("test")).thenReturn(stubWaiter);

        when(reservationRepository.findById(orderId)).thenReturn(
                java.util.Optional.ofNullable(new ReservationEntity().withTable(stubTable)
                        .withOrderEntity(orderEntity)));

        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(orderEntity));

        //when
        ReservationEntity reservationEntity = tableOperationFacade.deleteReservation(orderId, "test");

        //then
        assertThat(reservationEntity.getOrderEntity()).isNotNull();
        assertThat(reservationEntity.getOrderEntity().getWaiter()).isNull();
    }

    @Test
    void checkIfCanSuccessfullyUnssignBartenderToReservation() {
        //given
        Long orderId = 1L;
        UserEntity stubBartender = new UserEntity();
        stubBartender.setUsername("test");

        OrderEntity orderEntity = new OrderEntity().withBartender(stubBartender).withId(orderId);

        when(userRepository.findByUsername("test")).thenReturn(stubBartender);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.ofNullable(orderEntity));

        //when
        OrderEntity orderEntity1 = tableOperationFacade.deleteReservationKitchen(orderId, "test", "bartender");

        //then
        assertThat(orderEntity1.getBartender()).isNull();

    }

    @Test
    void checkIfCanSuccessfullyUnassignChefToReservation() {
        //given
        Long orderId = 1L;
        UserEntity stubChef = new UserEntity();
        stubChef.setUsername("test");

        OrderEntity orderEntity = new OrderEntity().withChef(stubChef).withId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.ofNullable(orderEntity));

        //when
        OrderEntity orderEntity1 = tableOperationFacade.deleteReservationKitchen(orderId, "test", "chef");

        //then
        assertThat(orderEntity1.getChef()).isNull();

    }

    @Test
    void checkIfExceptionThrownDeleteKitchen() {
        //given
        Long orderId = 1L;
        UserEntity stubChef = new UserEntity();
        stubChef.setUsername("test");

        OrderEntity orderEntity = new OrderEntity().withChef(stubChef).withId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.ofNullable(orderEntity));

        //when
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> tableOperationFacade.deleteReservationKitchen(orderId, "test", "chefaaa"));

        //then
        assertThat(exception.getMessage()).isEqualTo("Something went wrong.");

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
                () -> tableOperationFacade.deleteReservation(resId, "test"));

        //then
        assertThat(exception.getMessage()).isEqualTo("Something went wrong.");
    }

    @Test
    void checkIfExceptionThrown() {
        //given

        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> tableOperationFacade.throwException());

        //then
        assertThat(exception.getMessage()).isEqualTo("Something went wrong.");

    }
}