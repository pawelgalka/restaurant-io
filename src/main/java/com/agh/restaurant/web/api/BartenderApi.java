package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.OrderResponse;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public OrderEntity changeStateOfBeverage(@RequestParam Long orderId){
        return orderOperationFacade.completeBeverageOrder(orderId);
    }

    @GetMapping("/getBeverageOrders")
    public List<OrderResponse> beverageOrders(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String bartenderName = authentication.getName();
        return orderOperationFacade.getIncompleteBeveragesOrder(bartenderName);
    }

    @PatchMapping(value = "/assign")
    public OrderEntity assignReservationToBartender(@RequestParam Long orderId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return tableOperationFacade.assignReservationKitchen(orderId,username, "bartender");
    }

    @DeleteMapping(value = "/assignDelete")
    public ResponseEntity<Object> deleteReservationToBartender(@RequestParam Long orderId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try{
            tableOperationFacade.deleteReservationKitchen(orderId,username, "bartender");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
