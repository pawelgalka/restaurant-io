package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.FeedbackRaport;
import com.agh.restaurant.domain.model.RaportEntity;
import org.springframework.data.repository.CrudRepository;

public interface RaportRepository extends CrudRepository<RaportEntity, Long> {

}
