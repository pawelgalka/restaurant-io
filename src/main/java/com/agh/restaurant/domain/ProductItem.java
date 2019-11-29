package com.agh.restaurant.domain;

public class ProductItem {

    private String name;
    private Integer amount;

    public ProductItem(String name, Integer amount) {
        this.name = name;
        this.amount = amount;
    }

    public ProductItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
