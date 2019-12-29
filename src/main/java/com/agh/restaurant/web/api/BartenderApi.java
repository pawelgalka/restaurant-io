package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.OrderResponse;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bartender")
public class BartenderApi {
    Logger logger = LoggerFactory.getLogger(BartenderApi.class);

    @Autowired
    OrderOperationFacade orderOperationFacade;

    @Autowired
    TableOperationFacade tableOperationFacade;

    @PatchMapping("/changeState")
    public void changeStateOfBeverage(@RequestParam Long orderId){
        orderOperationFacade.completeBeverageOrder(orderId);
    }

    @GetMapping("/getBeverageOrders")
    public List<OrderResponse> beverageOrders(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String bartenderName = authentication.getName();
        logger.error(bartenderName);
        return orderOperationFacade.getIncompleteBeveragesOrder(bartenderName);
    }

    @PatchMapping(value = "/assign")
    public ReservationEntity assignReservationToBartender(@RequestParam Long reservationId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return tableOperationFacade.assignReservation(reservationId,username, "bartender");
    }

    @DeleteMapping(value = "/assignDelete")
    public void deleteReservationToBartender(@RequestParam Long reservationId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        tableOperationFacade.deleteReservation(reservationId,username, "bartender");
    }
}
