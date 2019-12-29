package com.agh.restaurant.domain.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity(name = "food")
@Table(name = "FOOD")
public class FoodEntity extends AbstractEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private FoodType dishOrDrink;
    private Boolean isAvailable;
    private Double price;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "food_product",
            joinColumns = @JoinColumn(name = "FOOD_ID_"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID_")
    )
    private List<ProductEntity> neededProducts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodType getDishOrDrink() {
        return dishOrDrink;
    }

    public void setDishOrDrink(FoodType dishOrDrink) {
        this.dishOrDrink = dishOrDrink;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<ProductEntity> getNeededProducts() {
        return neededProducts;
    }

    public void setNeededProducts(List<ProductEntity> neededProducts) {
        this.neededProducts = neededProducts;
    }

    public enum FoodType {
        DISH("DISH"),DRINK("DRINK");

        private final String type;

        FoodType(String type) {
            this.type = type;
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FoodEntity))
            return false;
        if (!super.equals(o))
            return false;
        FoodEntity that = (FoodEntity) o;
        return Objects.equals(getName(), that.getName()) &&
                getDishOrDrink() == that.getDishOrDrink() &&
                Objects.equals(isAvailable, that.isAvailable) &&
                Objects.equals(getPrice(), that.getPrice()) &&
                Objects.equals(getNeededProducts(), that.getNeededProducts());
    }

    @Override public int hashCode() {
        return Objects
                .hash(super.hashCode(), getName(), getDishOrDrink(), isAvailable, getPrice(), getNeededProducts());
    }
}
