package com.agh.restaurant.service.impl;

import com.agh.restaurant.config.SecurityConfig.Roles;
import com.agh.restaurant.domain.dao.TestRepository;
import com.agh.restaurant.domain.model.TestEntity;
import com.agh.restaurant.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service
public class TestServiceImpl implements TestService {

    private final static String COUNTER_TEST = "entity.test.";

    @Autowired
    private TestRepository testRepository;

    @Transactional
    @Secured(value = Roles.ROLE_CUSTOMER)
    public Collection<TestEntity> findAll() {
        return testRepository.findAll();
    }

    @Transactional
    @Secured(value = Roles.ROLE_CUSTOMER)
    public TestEntity create(String name) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("TestNameIsBlank");
        }
        //TODO Create event here

        TestEntity entity = new TestEntity(name);
        return testRepository.save(entity);
    }
}
