package com.agh.restaurant.service;

import com.agh.restaurant.domain.model.TestEntity;

import java.util.Collection;

public interface TestService {

    Collection<TestEntity> findAll();

    TestEntity create(String name);

}
