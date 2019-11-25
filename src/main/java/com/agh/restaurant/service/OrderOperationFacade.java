package com.agh.restaurant.service;

import com.agh.restaurant.domain.OrderRequest;

public interface OrderOperationFacade {
    void processOrder(OrderRequest orderRequest);

    void completeDishOrder(Long orderId);

    void completeBeverageOrder(Long orderId);
}
