package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.StageEnum;

import javax.persistence.*;
import java.util.List;

@Entity(name = "order_food")
@Table(name = "ORDER_FOOD")
public class OrderEntity extends AbstractEntity {

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

    @OneToOne(mappedBy = "orderEntity", fetch = FetchType.LAZY)
    private ReservationEntity reservationEntity;

    private StageEnum stage;

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

    public UserEntity getWaiter() {
        return waiter;
    }

    public void setWaiter(UserEntity waiter) {
        this.waiter = waiter;
    }

    public UserEntity getChef() {
        return chef;
    }

    public void setChef(UserEntity chef) {
        this.chef = chef;
    }

    public UserEntity getBartender() {
        return bartender;
    }

    public void setBartender(UserEntity bartender) {
        this.bartender = bartender;
    }

    public ReservationEntity getReservationEntity() {
        return reservationEntity;
    }

    public void setReservationEntity(ReservationEntity reservationEntity) {
        this.reservationEntity = reservationEntity;
    }

    public OrderEntity withId(Long id) {
        this.setId(id);
        return this;
    }

    public OrderEntity withBeverages(List<FoodEntity> beverages) {
        this.setBeverages(beverages);
        return this;
    }

    public OrderEntity withDishes(List<FoodEntity> dishes) {
        this.setDishes(dishes);
        return this;
    }

    public OrderEntity withStage(StageEnum stage){
        this.setStage(stage);
        return this;
    }

    public OrderEntity withWaiter(UserEntity waiter){
        this.setWaiter(waiter);
        return this;
    }

    @Override public String toString() {
        return "OrderEntity{" +
                ", waiter=" + waiter +
                ", chef=" + chef +
                ", bartender=" + bartender +
                ", dishes=" + dishes +
                ", beverages=" + beverages +
                ", reservationEntity=" + reservationEntity +
                ", stage=" + stage +
                '}';
    }
}
