package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.domain.OrderResponse;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bartender")
@Secured(value = { SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_BARTENDER})
public class BartenderApi {

    @Autowired
    OrderOperationFacade orderOperationFacade;

    @Autowired
    TableOperationFacade tableOperationFacade;

    @PatchMapping("/changeState")
    public void changeStateOfBeverage(@RequestParam Long orderId){
        orderOperationFacade.completeBeverageOrder(orderId);
    }

    @GetMapping("/getBeverageOrders")
    public List<OrderResponse> beverageOrders(@RequestAttribute("username") String bartenderName){
        return orderOperationFacade.getIncompleteBeveragesOrder(bartenderName);
    }

    @PatchMapping(value = "/assign")
    public ReservationEntity assignReservationToBartender(@RequestParam Long reservationId, @RequestAttribute("username") String username){
        return tableOperationFacade.assignReservation(reservationId,username, "bartender");
    }

    @DeleteMapping(value = "/assignDelete")
    public void deleteReservationToBartender(@RequestParam Long reservationId, @RequestAttribute("username") String username){
        tableOperationFacade.deleteReservation(reservationId,username, "bartender");
    }
}
