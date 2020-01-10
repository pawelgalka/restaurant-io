package com.agh.restaurant.service;

import com.agh.restaurant.domain.FeedbackPojo;
import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.OrderResponse;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;

import java.util.List;

public interface OrderOperationFacade {
    List<FoodEntity> getMenuList();

    OrderEntity processOrder(OrderRequest orderRequest);

    OrderEntity completeDishOrder(Long orderId);

    OrderEntity completeBeverageOrder(Long orderId);

    OrderEntity finalizeOrder(Long orderId);

    StageEnum getOrderStatus(Long orderId);

    OrderEntity createFeedback(FeedbackPojo feedbackPojo);

    Double getOrderPrice(Long orderId);

    List<OrderResponse> getIncompleteBeveragesOrder(String bartenderName);

    List<OrderResponse> getIncompleteDishesOrder(String chefName);
}
