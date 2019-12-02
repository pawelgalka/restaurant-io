package com.agh.restaurant.domain;

public class ProductItemBuilder {
    private String name;
    private Integer amount;

    public ProductItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProductItemBuilder setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public ProductItem createProductItem() {
        return new ProductItem(name, amount);
    }
}