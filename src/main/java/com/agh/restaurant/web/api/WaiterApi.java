package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.domain.FeedbackPojo;
import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.TableResponse;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/waiter")
@Secured(value = { SecurityConfig.Roles.ROLE_ADMIN, SecurityConfig.Roles.ROLE_WAITER, SecurityConfig.Roles.ROLE_MANAGER})
public class WaiterApi {

    @Autowired
    private TableOperationFacade tableOperationFacade;

    @Autowired
    private OrderOperationFacade orderOperationFacade;

    @GetMapping(value = "/tables")
    public List<TableResponse> getTables() {
        return tableOperationFacade.getAllTables();
    }

    @PatchMapping(value = "/assign")
    public void assignReservationToWaiter(@RequestParam Long reservationId, @RequestAttribute("username") String username){

        tableOperationFacade.assignReservationToWaiter(reservationId,username);
    }

    @PostMapping(value = "/order")
    public OrderEntity createOrder(@RequestBody OrderRequest orderRequest){
        return orderOperationFacade.processOrder(orderRequest);
    }

    @GetMapping(value = "/orderStatus")
    public StageEnum getOrderState(@RequestParam Long orderId){
        return orderOperationFacade.getOrderStatus(orderId);
    }

    @GetMapping(value = "/menu")
    public Map<FoodEntity.FoodType, List<FoodEntity>> getMenu() {
        return orderOperationFacade.getMenuList().stream().collect(Collectors.groupingBy(
               FoodEntity::getDishOrDrink));
    }

    @GetMapping(value = "/getPrice")
    public Double getPriceForOrder(@RequestParam Long orderId){
        return orderOperationFacade.getOrderPrice(orderId);
    }

    @PatchMapping(value = "/finalize")
    public void finalizeOrder(@RequestParam Long orderId){
        orderOperationFacade.finalizeOrder(orderId);
    }

    @PostMapping(value = "/clientFeedback")
    public void sendClientFeedback(@RequestBody FeedbackPojo feedbackPojo, @RequestParam Long orderId){
        orderOperationFacade.createFeedback(feedbackPojo,orderId);

    }
}
