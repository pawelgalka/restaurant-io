package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.FoodEntity;
import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public class OrderResponse {

    private String waiter;
    
    private String chef;

    private String bartender;

    private List<FoodEntity> dishes;

    private List<FoodEntity> beverages;
    public OrderResponse(OrderEntity x) {
        this.bartender = x.getBartender() == null ? null : x.getBartender().getEmail();
        this.waiter = x.getWaiter() == null ? null : x.getWaiter().getEmail();
        this.chef = x.getChef() == null ? null :x.getChef().getEmail();
        this.dishes = x.getDishes();
        this.beverages = x.getBeverages();
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

    public List<FoodEntity> getDishes() {
        return dishes;
    }

    public void setDishes(List<FoodEntity> dishes) {
        this.dishes = dishes;
    }

    public List<FoodEntity> getBeverages() {
        return beverages;
    }

    public void setBeverages(List<FoodEntity> beverages) {
        this.beverages = beverages;
    }
}
