package com.agh.restaurant.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.agh.restaurant.web.dto.test.TestJson;
import com.agh.restaurant.web.dto.test.TestRequestJson;
import com.agh.restaurant.web.facade.WebFacade;

import java.util.List;

@RestController
public class TestResource {

	@Autowired
	private WebFacade webFacade;

	@Value("${env}")
	private String environment;

	@RequestMapping(value = "/api/client/test", method = RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.CREATED)
	public TestJson apiTestCreate(@RequestBody TestRequestJson request) {
		return webFacade.createTest(request);
	}

	@RequestMapping(value = "/api/client/test", method = RequestMethod.GET)
	@ResponseStatus(code = HttpStatus.OK)
	public List<TestJson> apiGetTests() {
		return webFacade.getTaskList();
	}

}
