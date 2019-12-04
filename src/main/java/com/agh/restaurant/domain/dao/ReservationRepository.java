package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Long> {
//    List<ReservationEntity> getByTimeOfReservation_Date(LocalDate timeOfReservation_date);

    Optional<ReservationEntity> findById(Long reservationId);
}
