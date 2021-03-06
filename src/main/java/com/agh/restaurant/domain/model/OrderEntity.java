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

import com.agh.restaurant.domain.StageEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity(name = "order_food")
@Table(name = "ORDER_FOOD")
public class OrderEntity extends AbstractEntity implements Serializable {

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

    @OneToOne(mappedBy = "orderEntity", fetch = FetchType.LAZY)
    private FeedbackEntity feedbackEntity;

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

    public OrderEntity withChef(UserEntity waiter){
        this.setChef(waiter);
        return this;
    }

    public OrderEntity withBartender(UserEntity waiter){
        this.setBartender(waiter);
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

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OrderEntity))
            return false;
        if (!super.equals(o))
            return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(getWaiter(), that.getWaiter()) &&
                Objects.equals(getChef(), that.getChef()) &&
                Objects.equals(getBartender(), that.getBartender()) &&
                Objects.equals(getDishes(), that.getDishes()) &&
                Objects.equals(getBeverages(), that.getBeverages()) &&
                Objects.equals(getReservationEntity(), that.getReservationEntity()) &&
                Objects.equals(feedbackEntity, that.feedbackEntity) &&
                getStage() == that.getStage();
    }

    @Override public int hashCode() {
        return Objects.hash(super.hashCode(), getWaiter(), getChef(), getBartender(), getDishes(), getBeverages(),
                getReservationEntity(), feedbackEntity, getStage());
    }
}
