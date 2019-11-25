package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.dao.UserRepository;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.service.TableOperationFacade;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Override
    public List<TableEntity> getTableFreeAtCertainTime(LocalDateTime dateTime) {
        List<TableEntity> takenTables = reservationRepository.getByTimeOfReservationEquals(dateTime).stream().map(
                ReservationEntity::getTableReservation).collect(Collectors.toList());
        List<TableEntity> allTables = StreamSupport.stream(tableRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        allTables.removeAll(takenTables);
        return allTables;
    }

    @Override
    public List<TableEntity> getAllTables() {
        return Lists.newArrayList(tableRepository.findAll());
    }

    @Override
    public void assignTableToWaiter(Long tableId, Long waiterId) {
        TableEntity tableEntity = tableRepository.findOne(tableId);
        tableEntity.getWaiterEntities().add(userRepository.findById(waiterId));
        tableRepository.save(tableEntity);
    }

    @Override
    public void createTable() {
        tableRepository.save(new TableEntity());
    }
}
