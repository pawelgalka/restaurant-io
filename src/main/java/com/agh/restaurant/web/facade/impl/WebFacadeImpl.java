package com.agh.restaurant.web.facade.impl;

import com.agh.restaurant.config.auth.firebase.FirebaseTokenHolder;
import com.agh.restaurant.service.FirebaseService;
import com.agh.restaurant.service.UserService;
import com.agh.restaurant.service.impl.UserServiceImpl;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.agh.restaurant.web.config.WebConfig;
import com.agh.restaurant.web.facade.WebFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service(value = "webFacade")
public class WebFacadeImpl implements WebFacade {

	@Autowired(required = false)
	private FirebaseService firebaseService;

	@Autowired
	@Qualifier(value = UserServiceImpl.NAME)
	private UserService userService;

	@Autowired
	@Qualifier(WebConfig.MODEL_MAPPER)
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public void registerUser(String mail, String displayName, String password, String role) {
		userService.registerUser(new RegisterUserInit(mail, displayName, password, role));
	}
}
