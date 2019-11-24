package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.facade.DatabaseFacade;
import com.agh.restaurant.domain.model.RaportEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.web.facade.WebFacade;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Secured(value = {SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_MANAGER})
public class ManagerApi {

    private static Logger logger = Logger.getLogger(ManagerApi.class);

    private final WebFacade webFacade;

    private final DatabaseFacade databaseFacade;

    @Autowired
    private TableRepository tableRepository;

    public ManagerApi(WebFacade webFacade, DatabaseFacade databaseFacade) {
        this.webFacade = webFacade;
        this.databaseFacade = databaseFacade;
    }

    @PostMapping(value = "/api/management/signup")
    public void signUp(@RequestHeader String firebaseToken, @RequestHeader String role) {
        webFacade.registerUser(firebaseToken, role);
    }

    @PostMapping(value = "/api/management/addTable")
    public void addTable(){
        tableRepository.save(new TableEntity());
    }

    @PostMapping(value = "/api/management/addTables/{num}")
    public void addTables(@PathVariable("num") Integer numberOfTables){
        for (int i = 0; i<numberOfTables; i++){
            this.addTable();
        }
    }

    @GetMapping(value = "/feedbackEmployees")
    public RaportEntity getEmployeesFeedback(@RequestBody(required = false) LocalDateTime localDateTime) {
        if (localDateTime == null){
            return databaseFacade.getEmployeesFeedback(LocalDateTime.now());
        } else {
            return databaseFacade.getEmployeesFeedback(localDateTime);
        }

    }

    @GetMapping(value = "/feedbackDishes")
    public RaportEntity getDishesFeedback() {
        return databaseFacade.getDishesFeedback();
    }
}
