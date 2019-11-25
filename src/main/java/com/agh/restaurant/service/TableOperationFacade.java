package com.agh.restaurant.service;

import com.agh.restaurant.domain.model.TableEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface TableOperationFacade {
    List<TableEntity> getTableFreeAtCertainTime(LocalDateTime dateTime);

    List<TableEntity> getAllTables();

    void assignTableToWaiter(Long tableId, Long waiterId);

    void createTable();
}
