/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.domain.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "food")
@Table(name = "FOOD")
public class FoodEntity extends AbstractEntity implements Serializable {

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
