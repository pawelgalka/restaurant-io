package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.OrderEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private String waiter;

    private String chef;

    private String bartender;

    private List<FoodResponse> dishes;

    private List<FoodResponse> beverages;

    public OrderResponse(OrderEntity x) {
        this.bartender = x.getBartender() == null ? null : x.getBartender().getEmail();
        this.waiter = x.getWaiter() == null ? null : x.getWaiter().getEmail();
        this.chef = x.getChef() == null ? null : x.getChef().getEmail();
        this.dishes = x.getDishes() == null ? null : x.getDishes().stream().map(FoodResponse::new).collect(
                Collectors.toList());
        this.beverages = x.getBeverages() == null ? null : x.getBeverages().stream().map(FoodResponse::new).collect(
                Collectors.toList());
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public String getBartender() {
        return bartender;
    }

    public void setBartender(String bartender) {
        this.bartender = bartender;
    }

    public List<FoodResponse> getDishes() {
        return dishes;
    }

    public void setDishes(List<FoodResponse> dishes) {
        this.dishes = dishes;
    }

    public List<FoodResponse> getBeverages() {
        return beverages;
    }

    public void setBeverages(List<FoodResponse> beverages) {
        this.beverages = beverages;
    }
}
