package com.agh.restaurant.web.facade;

import com.agh.restaurant.web.dto.test.TestJson;
import com.agh.restaurant.web.dto.test.TestRequestJson;

import java.util.List;

public interface WebFacade {

	void registerUser(String firebaseToken, String role);

	TestJson createTest(TestRequestJson json);

	List<TestJson> getTaskList();

}
