package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.FoodEntity;

public class FoodResponse {

    private String name;
    private FoodEntity.FoodType dishOrDrink;
    private Double price;

    public FoodResponse(FoodEntity foodEntity){
        this.name = foodEntity.getName();
        this.dishOrDrink = foodEntity.getDishOrDrink();
        this.price = foodEntity.getPrice();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodEntity.FoodType getDishOrDrink() {
        return dishOrDrink;
    }

    public void setDishOrDrink(FoodEntity.FoodType dishOrDrink) {
        this.dishOrDrink = dishOrDrink;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
