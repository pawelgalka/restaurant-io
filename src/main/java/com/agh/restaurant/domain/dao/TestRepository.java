package com.agh.restaurant.domain.dao;

import com.agh.restaurant.domain.model.TestEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface TestRepository extends CrudRepository<TestEntity, Long> {
    Collection<TestEntity> findAll();
}
