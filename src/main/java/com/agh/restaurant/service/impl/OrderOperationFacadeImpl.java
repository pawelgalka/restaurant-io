package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.FeedbackPojo;
import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.dao.FeedbackRepository;
import com.agh.restaurant.domain.dao.FoodRepository;
import com.agh.restaurant.domain.dao.OrderRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.model.FeedbackEntity;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public List<FoodEntity> getMenuList(){
        return Lists.newArrayList(foodRepository.findAll());
    }

    @Override
    public StageEnum getOrderStatus(Long orderId) {
        return orderRepository.findOne(orderId).getStage();
    }

    @Override
    public OrderEntity processOrder(OrderRequest orderRequest) {
        List<FoodEntity> dishesEntities = orderRequest.getDishes().stream()
                .map(dishId -> foodRepository.findOne(dishId)).collect(Collectors.toList());

        List<FoodEntity> beverages = orderRequest.getBeverages().stream()
                .map(beverageId -> foodRepository.findOne(beverageId)).collect(
                        Collectors.toList());

        OrderEntity newOrder = new OrderEntity();
        newOrder.setBeverages(beverages);
        newOrder.setDishes(dishesEntities);
        newOrder.setOrderOfTable(tableRepository.findOne(orderRequest.getTableId()));
        newOrder.setStage(StageEnum.IN_PROGRESS);
        orderRepository.save(newOrder);
        return newOrder;
    }

    @Override
    public void completeDishOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findOne(orderId);
        orderEntity.setStage(orderEntity.getStage() == StageEnum.BEVERAGE_COMPLETE ? StageEnum.ALL_COMPLETE : StageEnum.DISH_COMPLETE);
        //TODO: alter material repository
        orderRepository.save(orderEntity);
    }

    @Override
    public void completeBeverageOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findOne(orderId);
        orderEntity.setStage(orderEntity.getStage() == StageEnum.DISH_COMPLETE ? StageEnum.ALL_COMPLETE : StageEnum.BEVERAGE_COMPLETE);
        //TODO: alter material repository
        orderRepository.save(orderEntity);
    }

    @Override
    public void finalizeOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findOne(orderId);
        orderEntity.setStage(StageEnum.FINALIZED);
        orderRepository.save(orderEntity);
    }

    @Override public void createFeedback(FeedbackPojo feedbackPojo, Long orderId) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setServiceGrade(feedbackPojo.getServiceGrade());
        feedbackEntity.setBeverageGrade(feedbackPojo.getBeverageGrade());
        feedbackEntity.setDishGrade(feedbackPojo.getDishGrade());
        feedbackEntity.setOrderId(orderId);
        finalizeOrder(orderId);
        feedbackRepository.save(feedbackEntity);
    }

    @Override
    public Double getOrderPrice(Long orderId) {
        OrderEntity orderEntity = orderRepository.findOne(orderId);
        List<FoodEntity> orderedItems = Stream.of(orderEntity.getBeverages(), orderEntity.getDishes())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return orderedItems.stream().map(FoodEntity::getPrice).collect(Collectors.toList()).stream()
                .mapToDouble(a -> a)
                .sum();
    }
}
