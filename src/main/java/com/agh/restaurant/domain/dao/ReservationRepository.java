package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.ReservationEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Long> {

    Optional<ReservationEntity> findById(Long reservationId);
}
