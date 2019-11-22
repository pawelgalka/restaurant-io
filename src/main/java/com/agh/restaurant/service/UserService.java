package com.agh.restaurant.service;

import com.agh.restaurant.domain.model.UserEntity;
import com.agh.restaurant.service.shared.RegisterUserInit;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserEntity registerUser(RegisterUserInit init);

}
