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
@RequestMapping("/api/cooker")
@Secured(value = { SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_COOKER})
public class ChefApi {

    @Autowired
    OrderOperationFacade orderOperationFacade;

    @Autowired
    TableOperationFacade tableOperationFacade;

    @PatchMapping("/changeState")
    public void changeStateOfDish(@RequestParam Long orderId){
        orderOperationFacade.completeDishOrder(orderId);
    }

    @GetMapping("/getDishOrders")
    public List<OrderResponse> dishOrders(@RequestAttribute("username") String chefName){
        return orderOperationFacade.getIncompleteDishesOrder(chefName);
    }

    @PatchMapping(value = "/assign")
    public ReservationEntity assignReservationToBartender(@RequestParam Long reservationId, @RequestAttribute("username") String username){
        return tableOperationFacade.assignReservation(reservationId,username, "chef");
    }

    @DeleteMapping(value = "/assignDelete")
    public void deleteReservationToBartender(@RequestParam Long reservationId, @RequestAttribute("username") String username){
        tableOperationFacade.deleteReservation(reservationId,username, "chef");
    }
}
