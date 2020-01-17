/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.*;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.agh.restaurant.service.TableOperationFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/waiter")
public class WaiterApi {

    private final TableOperationFacade tableOperationFacade;

    private final OrderOperationFacade orderOperationFacade;

    public WaiterApi(TableOperationFacade tableOperationFacade,
            OrderOperationFacade orderOperationFacade) {
        this.tableOperationFacade = tableOperationFacade;
        this.orderOperationFacade = orderOperationFacade;
    }

    @GetMapping(value = "/tables")
    public List<TableResponse> getTables() {
        return tableOperationFacade.getAllTables();
    }

    @PatchMapping(value = "/assign")
    public ReservationEntity assignReservationToWaiter(@RequestParam Long reservationId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return tableOperationFacade.assignReservation(reservationId,username);
    }

    @DeleteMapping(value = "/assignDelete")
    public ResponseEntity deleteReservationToWaiter(@RequestParam Long reservationId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try{
            tableOperationFacade.deleteReservation(reservationId,username);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    public Map<FoodEntity.FoodType, List<FoodResponse>> getMenu() {
        return orderOperationFacade.getMenuList().stream().map(FoodResponse::new).collect(Collectors.groupingBy(
               FoodResponse::getDishOrDrink));
    }

    @GetMapping(value = "/getPrice")
    public Double getPriceForOrder(@RequestParam Long orderId){
        return orderOperationFacade.getOrderPrice(orderId);
    }

    @PatchMapping(value = "/finalize")
    public OrderEntity finalizeOrder(@RequestParam Long orderId){
        return orderOperationFacade.finalizeOrder(orderId);
    }

    @PostMapping(value = "/clientFeedback")
    public OrderEntity sendClientFeedback(@RequestBody FeedbackPojo feedbackPojo){
        return orderOperationFacade.createFeedback(feedbackPojo);

    }
}
