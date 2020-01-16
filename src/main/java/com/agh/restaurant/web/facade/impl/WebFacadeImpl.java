package com.agh.restaurant.web.facade.impl;

import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.UserService;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.agh.restaurant.web.facade.WebFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class WebFacadeImpl implements WebFacade {

    @Autowired
    private UserService userService;

    @Transactional
    @Override
    public void registerUser(RegisterUserInit registrationUnit){
        userService.registerUser(registrationUnit);
    }

    @Transactional
    @Override
    public void updateUser(RegisterUserInit registrationUnit) {
        userService.updateUser(registrationUnit);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        userService.deleteUser(id);
    }

    @Transactional
    @Override
    public void deleteUserByUsername(String username) {
        userService.deleteUser(username);
    }

    @Override public List<UserEntity> getUsers() {
        return userService.getUsers();
    }
}
