package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.FoodEntity;

import java.util.List;

public class RestaurantMenuItem {
    private String name;
    private FoodEntity.FoodType foodType;
    private Double price;
    private List<String> itemsNeededNames;

    public RestaurantMenuItem(String name, FoodEntity.FoodType foodType, Double price,
            List<String> itemsNeededNames) {
        this.name = name;
        this.foodType = foodType;
        this.price = price;
        this.itemsNeededNames = itemsNeededNames;
    }

    public RestaurantMenuItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodEntity.FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodEntity.FoodType foodType) {
        this.foodType = foodType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getItemsNeededNames() {
        return itemsNeededNames;
    }

    public void setItemsNeededNames(List<String> itemsNeededNames) {
        this.itemsNeededNames = itemsNeededNames;
    }
}
