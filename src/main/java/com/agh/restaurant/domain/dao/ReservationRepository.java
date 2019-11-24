package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Long> {
    List<ReservationEntity> getByTimeOfReservationEquals(LocalDateTime timeOfReservation);
}
