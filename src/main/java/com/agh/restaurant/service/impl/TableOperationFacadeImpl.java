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
    public void assignReservationToWaiter(Long tableId, String username) {
        System.out.println(userRepository.findByUsername(username).getEmail());
        ReservationEntity reservationEntity = reservationRepository.findOne(tableId);
        if (reservationEntity.getTableReservation().getWaiterEntities().isEmpty() || reservationEntity
                .getTableReservation().getWaiterEntities().stream().anyMatch(o -> o.getUsername().equals(username))) {
            reservationEntity.getTableReservation().getWaiterEntities().add(userRepository.findByUsername(username));
            reservationRepository.save(reservationEntity);
        }
        else{
            throw new IllegalArgumentException("Reservation already has waiter assigned.");
        }
    }

    @Override
    public void createTable() {
        tableRepository.save(new TableEntity());
    }
}
