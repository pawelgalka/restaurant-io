package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
@Secured(SecurityConfig.Roles.ROLE_CUSTOMER)
public class CustomerApi {

    @PostMapping(value = "/reserve")
    public HttpStatus createReservation(){
        return HttpStatus.OK;
    }
}
