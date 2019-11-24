package com.agh.restaurant.domain.model;

import javax.persistence.*;
import java.util.List;

@Entity(name = "order_food")
@Table(name = "ORDER_FOOD")
public class OrderEntity extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name="TABLE_ID_")
    private TableEntity orderOfTable;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FoodEntity> dishes;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FoodEntity> beverages;
}
