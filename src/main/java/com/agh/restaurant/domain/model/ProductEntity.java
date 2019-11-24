package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.ProductStatus;

import javax.persistence.*;
import java.util.List;

@Entity(name = "product")
@Table(name = "PRODUCT")
public class ProductEntity extends AbstractEntity {

    @Column(name = "NAME_")
    private String name;

    @Column(name = "PRODUCT_STATUS_")
    private ProductStatus productStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<FoodEntity> usedInFoods;
}
