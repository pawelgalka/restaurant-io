package com.agh.restaurant.service;

import com.agh.restaurant.domain.TableResponse;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TableOperationFacade {
    List<TableEntity> getTableFreeAtCertainTime(LocalDateTime dateTime, Integer duration);

    List<TableResponse> getAllTables();

    ReservationEntity assignReservation(Long resId, String username);

    ReservationEntity deleteReservation(Long resId, String username);

    OrderEntity assignReservationKitchen(Long resId, String username, String type);

    OrderEntity deleteReservationKitchen(Long resId, String username, String type);

    TableEntity createTable();
}
