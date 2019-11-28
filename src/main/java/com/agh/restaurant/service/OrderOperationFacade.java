package com.agh.restaurant.service;

import com.agh.restaurant.domain.FeedbackPojo;
import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;

import java.util.List;

public interface OrderOperationFacade {
    List<FoodEntity> getMenuList();

    OrderEntity processOrder(OrderRequest orderRequest);

    void completeDishOrder(Long orderId);

    void completeBeverageOrder(Long orderId);

    void finalizeOrder(Long orderId);

    StageEnum getOrderStatus(Long orderId);

    void createFeedback(FeedbackPojo feedbackPojo, Long orderId);

    Double getOrderPrice(Long orderId);
}
