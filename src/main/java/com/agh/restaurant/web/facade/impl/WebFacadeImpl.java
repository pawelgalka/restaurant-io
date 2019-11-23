package com.agh.restaurant.web.facade.impl;

import com.agh.restaurant.config.SecurityConfig.Roles;
import com.agh.restaurant.config.auth.firebase.FirebaseTokenHolder;
import com.agh.restaurant.domain.model.TestEntity;
import com.agh.restaurant.service.FirebaseService;
import com.agh.restaurant.service.TestService;
import com.agh.restaurant.service.UserService;
import com.agh.restaurant.service.impl.UserServiceImpl;
import com.agh.restaurant.service.shared.RegisterUserInit;
import com.agh.restaurant.web.config.WebConfig;
import com.agh.restaurant.web.dto.test.TestJson;
import com.agh.restaurant.web.dto.test.TestRequestJson;
import com.agh.restaurant.web.facade.WebFacade;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;

@Service(value = "webFacade")
public class WebFacadeImpl implements WebFacade {

	@Autowired(required = false)
	private FirebaseService firebaseService;

	@Autowired
	private TestService testService;

	@Autowired
	@Qualifier(value = UserServiceImpl.NAME)
	private UserService userService;

	@Autowired
	@Qualifier(WebConfig.MODEL_MAPPER)
	private ModelMapper modelMapper;

	@Transactional
	@Override
	public void registerUser(String firebaseToken, String role) {
		if (isBlank(firebaseToken)) {
			throw new IllegalArgumentException("FirebaseTokenBlank");
		}
		FirebaseTokenHolder tokenHolder = firebaseService.parseToken(firebaseToken);
		userService.registerUser(new RegisterUserInit(tokenHolder.getUid(), tokenHolder.getEmail(), role));
	}

	@Transactional
	@Override
	@Secured(value = Roles.ROLE_CUSTOMER)
	public TestJson createTest(TestRequestJson json) {
		TestEntity testEntity = testService.create(json.getName());
		return modelMapper.map(testEntity, TestJson.class);
	}

	@Transactional
	@Override
	@Secured(value = Roles.ROLE_CUSTOMER)
	public List<TestJson> getTaskList() {
		Type listType = new TypeToken<List<TestJson>>() {
		}.getType();

		return modelMapper.map(testService.findAll(), listType);
	}
}
