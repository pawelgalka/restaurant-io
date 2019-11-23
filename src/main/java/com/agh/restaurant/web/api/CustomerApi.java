package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/customer")
@Secured(SecurityConfig.Roles.ROLE_CUSTOMER)
public class CustomerApi {

    @PostMapping(value = "/reserve")
    public HttpStatus createReservation(){
        return HttpStatus.OK;
    }

    @DeleteMapping(value = "/reserve")
    public HttpStatus deleteReservation(@RequestBody String customerName, @RequestBody Integer reservationId){
        return HttpStatus.OK;
    }

    @PatchMapping(value = "/reserve")
    public HttpStatus modifyReservation(@RequestBody String customerName, @RequestBody Integer reservationId, @RequestBody LocalDateTime newDate){
        return HttpStatus.OK;
    }
}
