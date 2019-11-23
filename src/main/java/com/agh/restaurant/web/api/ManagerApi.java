package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.domain.facade.DatabaseFacade;
import com.agh.restaurant.web.facade.WebFacade;
import org.apache.log4j.Logger;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api/management")
@Secured(value = {SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_MANAGER})
public class ManagerApi {

    private static Logger logger = Logger.getLogger(ManagerApi.class);

    private final WebFacade webFacade;

    private final DatabaseFacade databaseFacade;

    public ManagerApi(WebFacade webFacade, DatabaseFacade databaseFacade) {
        this.webFacade = webFacade;
        this.databaseFacade = databaseFacade;
    }

    @PostMapping(value = "/signup")
    public void signUp(@RequestHeader String firebaseToken, @RequestHeader String role) {
        webFacade.registerUser(firebaseToken, role);
    }

    @GetMapping(value = "/feedbackEmployees")
    public Object getEmployeesFeedback() {
        return databaseFacade.getEmployeesFeedback();
    }

    @GetMapping(value = "/feedbackDishes")
    public Object getDishesFeedback() {
        return databaseFacade.getDishesFeedback();
    }
}
