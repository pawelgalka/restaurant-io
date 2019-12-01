package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.OrderEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    Optional<OrderEntity> findById(Long orderId);
}
