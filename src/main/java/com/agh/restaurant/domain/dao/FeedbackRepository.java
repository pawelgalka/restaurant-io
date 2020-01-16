package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.FeedbackEntity;
import org.springframework.data.repository.CrudRepository;

public interface FeedbackRepository extends CrudRepository<FeedbackEntity, Long> {
}
