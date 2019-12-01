package com.agh.restaurant.service.impl;

import com.agh.restaurant.domain.FeedbackPojo;
import com.agh.restaurant.domain.OrderRequest;
import com.agh.restaurant.domain.ProductStatus;
import com.agh.restaurant.domain.StageEnum;
import com.agh.restaurant.domain.dao.*;
import com.agh.restaurant.domain.model.FeedbackEntity;
import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ProductEntity;
import com.agh.restaurant.service.OrderOperationFacade;
import com.google.api.client.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<FoodEntity> getMenuList(){
        return Lists.newArrayList(foodRepository.findAll());
    }

    @Override
    public StageEnum getOrderStatus(Long orderId) {
        return Objects.requireNonNull(orderRepository.findById(orderId).orElse(null)).getStage();
    }

    @Override
    public OrderEntity processOrder(OrderRequest orderRequest) {
        List<FoodEntity> dishesEntities = orderRequest.getDishes().stream()
                .map(dishId -> foodRepository.findById(dishId).orElse(null)).collect(Collectors.toList());

        List<FoodEntity> beverages = orderRequest.getBeverages().stream()
                .map(beverageId -> foodRepository.findById(beverageId).orElse(null)).collect(
                        Collectors.toList());

        OrderEntity newOrder = new OrderEntity();
        newOrder.setBeverages(beverages);
        newOrder.setDishes(dishesEntities);
        newOrder.setOrderOfTable(tableRepository.findById(orderRequest.getTableId()).orElse(null));
        newOrder.setStage(StageEnum.IN_PROGRESS);
        return orderRepository.save(newOrder);

    }

    @Override
    public OrderEntity completeDishOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        orderEntity.setStage(orderEntity.getStage() == StageEnum.BEVERAGE_COMPLETE ? StageEnum.ALL_COMPLETE : StageEnum.DISH_COMPLETE);
        orderEntity.getDishes().forEach(item -> item.getNeededProducts().forEach(neededProduct -> {
           alterProductsOfFoodItem(item, neededProduct);
        }));
        return orderRepository.save(orderEntity);
    }

    @Override
    public OrderEntity completeBeverageOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId).orElse(null);
        assert orderEntity != null;
        orderEntity.setStage(orderEntity.getStage() == StageEnum.DISH_COMPLETE ? StageEnum.ALL_COMPLETE : StageEnum.BEVERAGE_COMPLETE);
        orderEntity.getBeverages().forEach(item -> item.getNeededProducts().forEach(neededProduct -> {
            alterProductsOfFoodItem(item, neededProduct);
        }));
        return orderRepository.save(orderEntity);
    }

    private void alterProductsOfFoodItem(FoodEntity item, ProductEntity neededProduct) {
        ProductEntity productEntity = productRepository.findById(neededProduct.getId()).orElse(null);
        productEntity.setAmount(productEntity.getAmount() - 1);
        System.out.println(productEntity.getAmount());
        if (productEntity.getAmount() >= 10 && !ProductStatus.AVAILABLE.equals(productEntity.getProductStatus())){
            productEntity.setProductStatus(ProductStatus.AVAILABLE);
        } else if (productEntity.getAmount() < 10 && productEntity.getAmount() > 0){
            productEntity.setProductStatus(ProductStatus.LOW);
        } else if (productEntity.getAmount() == 0){
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
}
