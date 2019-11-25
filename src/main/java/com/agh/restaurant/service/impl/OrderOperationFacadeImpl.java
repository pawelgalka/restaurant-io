package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.dao.FoodRepository;
import com.agh.restaurant.domain.dao.OrderRepository;
import com.agh.restaurant.domain.dao.TableRepository;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderOperationFacadeImpl implements OrderOperationFacade {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    TableRepository tableRepository;

    @Autowired
    OrderRepository orderRepository;

    @Override
    public void processOrder(OrderRequest orderRequest) {
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
}
