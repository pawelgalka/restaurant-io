package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.OrderResponse;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public OrderEntity changeStateOfDish(@RequestParam Long orderId){
        return orderOperationFacade.completeDishOrder(orderId);
    }

    @GetMapping("/getDishOrders")
    public List<OrderResponse> dishOrders(){
        String username = getUsername();

        return orderOperationFacade.getIncompleteDishesOrder(username);
    }

    @PatchMapping(value = "/assign")
    public OrderEntity assignReservationToBartender(@RequestParam Long orderId){
        String username = getUsername();

        return tableOperationFacade.assignReservationKitchen(orderId,username, "chef");
    }

    @DeleteMapping(value = "/assignDelete")
    public ResponseEntity<Object> deleteReservationToBartender(@RequestParam Long orderId){
        String username = getUsername();

        try{
            tableOperationFacade.deleteReservationKitchen(orderId,username, "chef");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
