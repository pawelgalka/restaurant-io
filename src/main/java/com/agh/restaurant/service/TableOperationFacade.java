package com.agh.restaurant.service;

import com.agh.restaurant.domain.TableResponse;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TableOperationFacade {
    List<TableEntity> getTableFreeAtCertainTime(LocalDateTime dateTime, Integer duration);

    List<TableResponse> getAllTables();

    ReservationEntity assignReservationToWaiter(Long tableId, String username);

    ReservationEntity deleteReservationToWaiter(Long resId, String username);

    TableEntity createTable();
}
