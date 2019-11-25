package com.agh.restaurant.domain;

import java.util.List;

public class OrderRequest {
    List<Long> dishes;
    List<Long> beverages;
    Long tableId;

    public OrderRequest(List<Long> dishes, List<Long> beverages, Long tableId) {
        this.dishes = dishes;
        this.beverages = beverages;
        this.tableId = tableId;
    }

    public OrderRequest() {
    }

    public List<Long> getDishes() {
        return dishes;
    }

    public void setDishes(List<Long> dishes) {
        this.dishes = dishes;
    }

    public List<Long> getBeverages() {
        return beverages;
    }

    public void setBeverages(List<Long> beverages) {
        this.beverages = beverages;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }
}
