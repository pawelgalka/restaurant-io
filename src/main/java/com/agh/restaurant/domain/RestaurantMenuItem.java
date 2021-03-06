/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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

    @Override public String toString() {
        return "RestaurantMenuItem{" +
                "name='" + name + '\'' +
                ", foodType=" + foodType +
                ", price=" + price +
                ", itemsNeededNames=" + itemsNeededNames +
                '}';
    }
}
