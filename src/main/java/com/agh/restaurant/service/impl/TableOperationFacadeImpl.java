/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
            int startTaken = reservationEntity.getTimeOfReservation().toLocalTime().toSecondOfDay();
            int endTaken = reservationEntity.getTimeOfReservation().toLocalTime().plusHours(reservationEntity.getDuration())
                    .toSecondOfDay();
            int startNew = dateTime.toLocalTime().toSecondOfDay();
            int endNew = dateTime.toLocalTime().plusHours(duration).toSecondOfDay();

            return reservationEntity.getTimeOfReservation().toLocalDate().equals(dateTime.toLocalDate()) && (
                    (startTaken <= endNew)  &&  (endTaken >= startNew));
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
