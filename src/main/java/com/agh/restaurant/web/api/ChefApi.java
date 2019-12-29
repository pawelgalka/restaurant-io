package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.OrderResponse;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cook")
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
    public List<OrderResponse> dishOrders(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chefName = authentication.getName();
        return orderOperationFacade.getIncompleteDishesOrder(chefName);
    }

    @PatchMapping(value = "/assign")
    public ReservationEntity assignReservationToBartender(@RequestParam Long reservationId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return tableOperationFacade.assignReservation(reservationId,username, "chef");
    }

    @DeleteMapping(value = "/assignDelete")
    public void deleteReservationToBartender(@RequestParam Long reservationId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        tableOperationFacade.deleteReservation(reservationId,username, "chef");
    }
}
