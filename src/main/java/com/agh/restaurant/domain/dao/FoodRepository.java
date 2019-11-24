package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.FoodEntity;
import org.springframework.data.repository.CrudRepository;

public interface FoodRepository extends CrudRepository<FoodEntity, Long> {
}
