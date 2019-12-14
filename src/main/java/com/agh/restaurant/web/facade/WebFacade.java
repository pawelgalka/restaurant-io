package com.agh.restaurant.web.facade;

import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.shared.RegisterUserInit;

import java.util.List;

public interface WebFacade {

    void registerUser(RegisterUserInit registrationUnit);

    void updateUser(RegisterUserInit registrationUnit);

    void deleteUser(Long id);

    List<UserEntity> getUsers();

}
