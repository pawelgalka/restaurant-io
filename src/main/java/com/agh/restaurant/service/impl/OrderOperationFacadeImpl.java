/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.*;
import com.agh.restaurant.domain.dao.*;
import com.agh.restaurant.domain.model.*;
import com.agh.restaurant.service.OrderOperationFacade;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Service
public class OrderOperationFacadeImpl implements OrderOperationFacade {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Override
    public List<FoodEntity> getMenuList() {
        return Lists.newArrayList(foodRepository.findAll());
    }

    @Override
    public StageEnum getOrderStatus(Long orderId) {
        return Objects.requireNonNull(orderRepository.findById(orderId).orElse(null)).getStage();
    }

    @Override
    public OrderEntity processOrder(OrderRequest orderRequest) {
        List<FoodEntity> dishesEntities = orderRequest.getDishes().stream()
                .map(dishId -> foodRepository.findById(dishId).orElse(null)).filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<FoodEntity> beverages = orderRequest.getBeverages().stream()
                .map(beverageId -> foodRepository.findById(beverageId).orElse(null)).filter(Objects::nonNull).collect(
                        Collectors.toList());

        ReservationEntity reservationEntityOptional = reservationRepository.findById(orderRequest.getReservationId())
                .orElse(null);

        OrderEntity newOrder = Objects.requireNonNull(reservationEntityOptional).getOrderEntity();
        newOrder.setBeverages(beverages);
        newOrder.setDishes(dishesEntities);
        newOrder.setReservationEntity(reservationRepository.findById(orderRequest.getReservationId()).orElse(null));
        newOrder.setStage(StageEnum.IN_PROGRESS);
        return orderRepository.save(newOrder);

    }

    @Override
    public OrderEntity completeDishOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        assert orderEntity != null;
        orderEntity.setStage(orderEntity.getStage() == StageEnum.BEVERAGE_COMPLETE ?
                StageEnum.ALL_COMPLETE :
                StageEnum.DISH_COMPLETE);
        orderEntity.getDishes().forEach(item -> item.getNeededProducts().forEach(neededProduct ->
                alterProductsOfFoodItem(item, neededProduct)));
        return orderRepository.save(orderEntity);
    }

    @Override
    public OrderEntity completeBeverageOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        assert orderEntity != null;
        orderEntity.setStage(orderEntity.getStage() == StageEnum.DISH_COMPLETE ?
                StageEnum.ALL_COMPLETE :
                StageEnum.BEVERAGE_COMPLETE);
        orderEntity.getBeverages().forEach(item -> item.getNeededProducts().forEach(neededProduct ->
                alterProductsOfFoodItem(item, neededProduct)));
        return orderRepository.save(orderEntity);
    }

    private void alterProductsOfFoodItem(FoodEntity item, ProductEntity neededProduct) {
        ProductEntity productEntity = productRepository.findById(neededProduct.getId()).orElse(null);
        assert productEntity != null;
        productEntity.setAmount(productEntity.getAmount() - 1);
        if (productEntity.getAmount() >= 10 && !ProductStatus.AVAILABLE.equals(productEntity.getProductStatus())) {
            productEntity.setProductStatus(ProductStatus.AVAILABLE);
        } else if (productEntity.getAmount() < 10 && productEntity.getAmount() > 0) {
            productEntity.setProductStatus(ProductStatus.LOW);
        } else if (productEntity.getAmount() == 0) {
            productEntity.setProductStatus(ProductStatus.NOT_AVAILABLE);
            item.setAvailable(false);
            foodRepository.save(item);
        }
        productRepository.save(productEntity);

    }

    @Override
    public OrderEntity finalizeOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        assert orderEntity != null;
        orderEntity.setStage(StageEnum.FINALIZED);
        return orderRepository.save(orderEntity);
    }

    @Override public OrderEntity createFeedback(FeedbackPojo feedbackPojo) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        OrderEntity orderEntity = orderRepository.findById(feedbackPojo.getOrderId()).orElse(null);
        feedbackEntity.setServiceGrade(feedbackPojo.getServiceGrade());
        feedbackEntity.setBeverageGrade(feedbackPojo.getBeverageGrade());
        feedbackEntity.setDishGrade(feedbackPojo.getDishGrade());
        feedbackEntity.setOrderEntity(orderEntity);
        finalizeOrder(feedbackPojo.getOrderId());
        feedbackRepository.save(feedbackEntity);
        return orderEntity;
    }

    @Override
    public Double getOrderPrice(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        assert orderEntity != null;
        List<FoodEntity> orderedItems = Stream.of(
                Optional.ofNullable(orderEntity.getBeverages()).orElse(Collections.emptyList()),
                Optional.ofNullable(orderEntity.getDishes()).orElse(Collections.emptyList()))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return orderedItems.stream().map(FoodEntity::getPrice).collect(Collectors.toList()).stream()
                .mapToDouble(a -> a)
                .sum();
    }

    @Override
    public List<OrderResponse> getIncompleteBeveragesOrder(String bartenderName) {
        List<OrderEntity> orderEntities = Lists.newArrayList(orderRepository.findAll());
        return orderEntities.stream()
                .filter(orderEntity -> isNotEmpty(
                        orderEntity.getBeverages()) && (
                        StageEnum.IN_PROGRESS.equals(orderEntity.getStage()) || StageEnum.DISH_COMPLETE
                                .equals(orderEntity.getStage()))
                ).map(OrderResponse::new).collect(Collectors.toList());
    }

    @Override public List<OrderResponse> getIncompleteDishesOrder(String chefName) {
        List<OrderEntity> orderEntities = Lists.newArrayList(orderRepository.findAll());
        return orderEntities.stream().filter(orderEntity ->
                isNotEmpty(orderEntity.getDishes()) && (
                        StageEnum.IN_PROGRESS.equals(orderEntity.getStage()) || StageEnum.BEVERAGE_COMPLETE
                                .equals(orderEntity.getStage()))
        ).map(OrderResponse::new).collect(Collectors.toList());

    }
}
