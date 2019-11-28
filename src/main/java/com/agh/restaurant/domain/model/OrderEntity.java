package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.StageEnum;

import javax.persistence.*;
import java.util.List;

@Entity(name = "order_food")
@Table(name = "ORDER_FOOD")
public class OrderEntity extends AbstractEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TABLE_ID_")
    private TableEntity orderOfTable;

    @ManyToOne
    @JoinColumn(name = "WAITER_ID_")
    private UserEntity waiter;

    @ManyToOne
    @JoinColumn(name = "CHEF_ID_")
    private UserEntity chef;

    @ManyToOne
    @JoinColumn(name = "BARTENDER_ID_")
    private UserEntity bartender;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FoodEntity> dishes;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FoodEntity> beverages;

    private StageEnum stage;

    public TableEntity getOrderOfTable() {
        return orderOfTable;
    }

    public void setOrderOfTable(TableEntity orderOfTable) {
        this.orderOfTable = orderOfTable;
    }

    public List<FoodEntity> getDishes() {
        return dishes;
    }

    public void setDishes(List<FoodEntity> dishes) {
        this.dishes = dishes;
    }

    public List<FoodEntity> getBeverages() {
        return beverages;
    }

    public void setBeverages(List<FoodEntity> beverages) {
        this.beverages = beverages;
    }

    public StageEnum getStage() {
        return stage;
    }

    public void setStage(StageEnum stage) {
        this.stage = stage;
    }
}
