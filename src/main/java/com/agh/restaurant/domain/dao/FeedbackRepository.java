package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.FeedbackEnum;
import com.agh.restaurant.domain.model.FeedbackEntity;
import org.springframework.data.repository.CrudRepository;

public interface FeedbackRepository extends CrudRepository<FeedbackEntity, Long> {

    FeedbackEntity findByDishGrade(FeedbackEnum dishGrade);

    FeedbackEntity findByBeverageGrade(FeedbackEnum beverageGrade);

    FeedbackEntity findByServiceGrade(FeedbackEnum serviceGrade);

}
