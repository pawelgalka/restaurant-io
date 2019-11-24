package com.agh.restaurant.domain.model;

import javax.persistence.*;
import java.util.List;

@Entity(name = "food")
@Table(name = "FOOD")
public class FoodEntity extends AbstractEntity {

    private Boolean dishOrDrink;
    private Boolean isAvailable;
    private Integer price;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<ProductEntity> neededProducts;
}
