/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
