package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.TableResponse;
import com.agh.restaurant.domain.dao.OrderRepository;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.service.TableOperationFacade;
import com.google.common.collect.Lists;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TableOperationFacadeImpl implements TableOperationFacade {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public List<TableEntity> getTableFreeAtCertainTime(LocalDateTime dateTime, Integer duration) {
        List<ReservationEntity> reservationsAtDate = Lists
                .newArrayList(reservationRepository.findAll()).stream()
                .filter(x -> x.getTimeOfReservation().toLocalDate().equals(dateTime.toLocalDate())).collect(
                        Collectors.toList());
        List<TableEntity> takenTablesAtDateAndDuration = getTakenTableEntities(dateTime, duration, reservationsAtDate);
        List<TableEntity> allTables = StreamSupport.stream(tableRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        allTables.removeAll(takenTablesAtDateAndDuration);
        return allTables;
    }

    public List<TableEntity> getTakenTableEntities(LocalDateTime dateTime, Integer duration,
            List<ReservationEntity> reservationsAtDate) {

        return reservationsAtDate.stream().filter(reservationEntity -> {
            Interval reservationInterval = new Interval(
                    reservationEntity.getTimeOfReservation().toLocalTime().toSecondOfDay(),
                    reservationEntity.getTimeOfReservation().toLocalTime().plusHours(reservationEntity.getDuration())
                            .toSecondOfDay());

            Interval newInterval = new Interval(dateTime.toLocalTime().toSecondOfDay(),
                    dateTime.toLocalTime().plusHours(duration).toSecondOfDay());
            return reservationEntity.getTimeOfReservation().toLocalDate().equals(dateTime.toLocalDate()) && !(
                    reservationInterval.getEnd().isBefore(newInterval.getStart()) || reservationInterval.getStart()
                            .isAfter(newInterval.getEnd()));
        }).map(ReservationEntity::getTableReservation).collect(Collectors.toList());
    }

    @Override
    public List<TableResponse> getAllTables() {
        return Lists.newArrayList(tableRepository.findAll()).stream().map(TableResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public ReservationEntity assignReservation(Long resId, String username) {
        ReservationEntity reservationEntity = reservationRepository.findById(resId).orElse(null);
        assert reservationEntity != null;
        OrderEntity orderEntity;
        if (reservationEntity.getOrderEntity() == null) {
            orderEntity = new OrderEntity();
        } else {
            orderEntity = reservationEntity.getOrderEntity();
        }
        if (orderEntity.getWaiter() == null) {
            orderEntity.setWaiter(userRepository.findByUsername(username));
        } else
            throwException();

        reservationEntity.setOrderEntity(orderEntity);
        orderRepository.save(orderEntity);
        reservationRepository.save(reservationEntity);
        return reservationEntity;
    }

    @Override
    public OrderEntity assignReservationKitchen(Long resId, String username, String type) {
        OrderEntity orderEntity = orderRepository.findById(resId).orElse(null);
        assert orderEntity != null;

        switch (type) {
            case "bartender":
                if (orderEntity.getBartender() == null) {
                    orderEntity.setBartender(userRepository.findByUsername(username));
                } else
                    throwException();
                break;
            case "chef":
                if (orderEntity.getChef() == null) {
                    orderEntity.setChef(userRepository.findByUsername(username));
                } else
                    throwException();
                break;
            default:
                throwException();
        }
        orderRepository.save(orderEntity);
        return orderEntity;
    }

    @Override
    public ReservationEntity deleteReservation(Long resId, String username) {
        ReservationEntity reservationEntity = reservationRepository.findById(resId).orElse(null);
        assert reservationEntity != null;
        if (reservationEntity.getOrderEntity() != null) {
            OrderEntity orderEntity = orderRepository.findById(reservationEntity.getOrderEntity().getId()).orElse(null);
            assert orderEntity != null;
            orderEntity.setWaiter(null);

            reservationRepository.save(reservationEntity);
        } else {
            throwException();
        }
        return reservationEntity;
    }

    @Override
    public OrderEntity deleteReservationKitchen(Long resId, String username, String type) {
        OrderEntity orderEntity = orderRepository.findById(resId).orElse(null);
        assert orderEntity != null;
        switch (type) {
            case "bartender":
                if (orderEntity.getBartender() == null){
                    throwException();
                }
                orderEntity.setBartender(null);
                break;
            case "chef":
                if (orderEntity.getChef() == null){
                    throwException();
                }
                orderEntity.setChef(null);
                break;
            default:
                throwException();
        }
        orderRepository.save(orderEntity);
        return orderEntity;
    }

    @Override
    public TableEntity createTable() { return tableRepository.save(new TableEntity().withTableReservations(new ArrayList<>())); }

    public void throwException() {
        throw new IllegalArgumentException("Something went wrong.");
    }
}
