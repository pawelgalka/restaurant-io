package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.ReservationEntity;
import org.springframework.data.repository.CrudRepository;

public interface ReservationRepository extends CrudRepository<ReservationEntity, Long> {
}
