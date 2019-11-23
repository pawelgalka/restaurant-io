package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.FeedbackEnum;
import com.agh.restaurant.domain.model.FeedbackEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedbackRepository extends CrudRepository<FeedbackEntity, Long> {

    List<FeedbackEntity> findByDishGrade(FeedbackEnum dishGrade);

    List<FeedbackEntity> findByBeverageGrade(FeedbackEnum beverageGrade);

    List<FeedbackEntity> findByServiceGrade(FeedbackEnum serviceGrade);

}
