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

import com.agh.restaurant.domain.model.OrderEntity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse implements Serializable {

    private Long id;

    private String waiter;

    private String chef;

    private String bartender;

    private List<FoodResponse> dishes;

    private List<FoodResponse> beverages;

    private StageEnum stage;

    public OrderResponse(OrderEntity x) {
        this.id = x.getId();
        this.bartender = x.getBartender() == null ? null : x.getBartender().getUsername();
        this.waiter = x.getWaiter() == null ? null : x.getWaiter().getUsername();
        this.chef = x.getChef() == null ? null : x.getChef().getUsername();
        this.dishes = x.getDishes() == null ? null : x.getDishes().stream().map(FoodResponse::new).collect(
                Collectors.toList());
        this.beverages = x.getBeverages() == null ? null : x.getBeverages().stream().map(FoodResponse::new).collect(
                Collectors.toList());
        this.stage = x.getStage();
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StageEnum getStage() {
        return stage;
    }

    public void setStage(StageEnum stage) {
        this.stage = stage;
    }
}
