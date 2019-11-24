package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.service.TableFinderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TableFinderServiceImpl implements TableFinderService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TableRepository tableRepository;

    @Override
    public List<TableEntity> getTableFreeAtCertainTime(LocalDateTime dateTime) {
        List<TableEntity> takenTables = reservationRepository.getByTimeOfReservationEquals(dateTime).stream().map(
                ReservationEntity::getTableReservation).collect(Collectors.toList());
        List<TableEntity> allTables = StreamSupport.stream(tableRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());

        allTables.removeAll(takenTables);
        return allTables;
    }
}
