package com.agh.restaurant.domain.facade;

import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.RaportEntity;
import com.agh.restaurant.domain.model.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DatabaseFacade {
    abstract List<RaportEntity> getEmployeesFeedback(LocalDateTime now);

    RaportEntity getDishesFeedback(LocalDateTime localDateTime);

    RaportEntity getBeveragesFeedback(LocalDateTime localDateTime);

    abstract Map<UserEntity, Double> createEmployeesFeedback(LocalDateTime dateTime);

    Map<FoodEntity, Integer> createDishesFeedback(LocalDateTime dateTime);

    Map<FoodEntity, Integer> createBeveragesFeedback(LocalDateTime dateTime);
}
