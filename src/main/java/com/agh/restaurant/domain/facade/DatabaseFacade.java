package com.agh.restaurant.domain.facade;

public interface DatabaseFacade {
    Object getEmployeesFeedback();

    Object getDishesFeedback();

    void createEmployeesFeedback();

    void createDishesFeedback();
}
