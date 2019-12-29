package com.agh.restaurant.service;

import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.shared.RegisterUserInit;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserEntity registerUser(RegisterUserInit init);

    UserEntity updateUser(RegisterUserInit registrationUnit);

    void deleteUser(Long id);

    void deleteUser(String username);

    List<UserEntity> getUsers();
}
