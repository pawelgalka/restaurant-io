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
