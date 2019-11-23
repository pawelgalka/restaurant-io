package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.agh.restaurant.web.facade.WebFacade;

@RestController
@Secured(value = {SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_MANAGER})
public class SignupResource {

	private static Logger logger = Logger.getLogger(SignupResource.class);

	@Autowired
	private WebFacade facade;

	@RequestMapping(value = "/api/management/signup", method = RequestMethod.POST)
	public void signUp(@RequestHeader String firebaseToken, @RequestHeader String role) {
		logger.info("token" + firebaseToken);
		facade.registerUser(firebaseToken, role);
	}
}
