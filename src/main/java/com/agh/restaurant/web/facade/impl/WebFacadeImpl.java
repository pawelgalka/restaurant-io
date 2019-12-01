package com.agh.restaurant.web.facade.impl;

import com.agh.restaurant.service.FirebaseService;
import com.agh.restaurant.service.UserService;
import com.agh.restaurant.service.impl.UserServiceImpl;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.agh.restaurant.web.facade.WebFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service(value = "webFacade")
public class WebFacadeImpl implements WebFacade {

	@Autowired(required = false)
	private FirebaseService firebaseService;

	@Autowired
	@Qualifier(value = UserServiceImpl.NAME)
	private UserService userService;

	@Transactional
    @Override
    public void registerUser(RegisterUserInit registrationUnit) {
        userService.registerUser(registrationUnit);
    }
}
