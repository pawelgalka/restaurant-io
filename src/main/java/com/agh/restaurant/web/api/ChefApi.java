package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;

import com.agh.restaurant.service.OrderOperationFacade;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cooker")
@Secured(value = { SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_COOKER})
public class ChefApi {
    Logger logger = Logger.getLogger(ChefApi.class);

    @Autowired
    OrderOperationFacade orderOperationFacade;

    @PatchMapping("/changeState")
    public void changeStateOfDish(@RequestParam Long orderId){
        orderOperationFacade.completeDishOrder(orderId);
    }
}
