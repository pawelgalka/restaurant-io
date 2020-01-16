package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.FoodEntity;

import java.io.Serializable;

public class FoodResponse implements Serializable {

    private Long id;
    private String name;
    private FoodEntity.FoodType dishOrDrink;
    private Double price;

    public FoodResponse(FoodEntity foodEntity){
        this.id = foodEntity.getId();
        this.name = foodEntity.getName();
        this.dishOrDrink = foodEntity.getDishOrDrink();
        this.price = foodEntity.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
