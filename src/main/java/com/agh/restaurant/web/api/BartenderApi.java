package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.service.OrderOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bartender")
@Secured(value = { SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_BARTENDER})
public class BartenderApi {

    @Autowired
    OrderOperationFacade orderOperationFacade;

    @PatchMapping("/changeState")
    public void changeStateOfBeverage(@RequestParam Long orderId){
        orderOperationFacade.completeBeverageOrder(orderId);
    }
}
