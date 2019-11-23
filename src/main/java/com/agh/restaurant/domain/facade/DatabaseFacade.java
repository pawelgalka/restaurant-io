package com.agh.restaurant.domain.facade;

import com.agh.restaurant.domain.model.RaportEntity;

import java.time.LocalDateTime;

public interface DatabaseFacade {
    RaportEntity getEmployeesFeedback(LocalDateTime now);

    RaportEntity getDishesFeedback();

    void createEmployeesFeedback();

    void createDishesFeedback();
}
