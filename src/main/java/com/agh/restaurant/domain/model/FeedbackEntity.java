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

import com.agh.restaurant.domain.FeedbackEnum;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity(name = "feedback")
@Table(name = "FEEDBACK")
public class FeedbackEntity extends AbstractEntity implements Serializable {

    public FeedbackEntity(OrderEntity orderEntity, FeedbackEnum serviceGrade, FeedbackEnum beverageGrade,
            FeedbackEnum dishGrade) {
        this.serviceGrade = serviceGrade;
        this.beverageGrade = beverageGrade;
        this.dishGrade = dishGrade;
        this.orderEntity = orderEntity;
    }

    public FeedbackEntity() {
    }

    @OneToOne
    @JoinColumn(name = "ORDER_ID_")
    private OrderEntity orderEntity;

    @Column(name = "SERVICE_GRADE_")
    private FeedbackEnum serviceGrade;

    @Column(name = "BEVERAGE_GRADE_")
    private FeedbackEnum beverageGrade;

    @Column(name = "DISH_GRADE_")
    private FeedbackEnum dishGrade;

    public FeedbackEnum getServiceGrade() {
        return serviceGrade;
    }

    public void setServiceGrade(FeedbackEnum serviceGrade) {
        this.serviceGrade = serviceGrade;
    }

    public FeedbackEnum getBeverageGrade() {
        return beverageGrade;
    }

    public void setBeverageGrade(FeedbackEnum beverageGrade) {
        this.beverageGrade = beverageGrade;
    }

    public FeedbackEnum getDishGrade() {
        return dishGrade;
    }

    public void setDishGrade(FeedbackEnum dishGrade) {
        this.dishGrade = dishGrade;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof FeedbackEntity))
            return false;
        if (!super.equals(o))
            return false;
        FeedbackEntity that = (FeedbackEntity) o;
        return Objects.equals(getOrderEntity(), that.getOrderEntity()) &&
                getServiceGrade() == that.getServiceGrade() &&
                getBeverageGrade() == that.getBeverageGrade() &&
                getDishGrade() == that.getDishGrade();
    }

    @Override public int hashCode() {
        return Objects.hash(super.hashCode(), getOrderEntity(), getServiceGrade(), getBeverageGrade(), getDishGrade());
    }

    @Override public String toString() {
        return "FeedbackEntity{" +
                "orderEntity=" + orderEntity.getId() +
                ", serviceGrade=" + serviceGrade +
                ", beverageGrade=" + beverageGrade +
                ", dishGrade=" + dishGrade +
                '}';
    }
}
