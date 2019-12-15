package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.FeedbackEnum;

import javax.persistence.*;

@Entity(name = "feedback")
@Table(name = "FEEDBACK")
public class FeedbackEntity extends AbstractEntity{

    public FeedbackEntity(OrderEntity orderId, FeedbackEnum serviceGrade, FeedbackEnum beverageGrade, FeedbackEnum dishGrade) {
        this.serviceGrade = serviceGrade;
        this.beverageGrade = beverageGrade;
        this.dishGrade = dishGrade;
        this.orderEntity = orderId;
    }

    public FeedbackEntity() {
    }

    @OneToOne
    @JoinColumn(name = "ORDER_ID_")
    private OrderEntity orderEntity;

    @Column(name = "SERVICE_GRADE_")
    FeedbackEnum serviceGrade;

    @Column(name = "BEVERAGE_GRADE_")
    FeedbackEnum beverageGrade;

    @Column(name = "DISH_GRADE_")
    FeedbackEnum dishGrade;


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
}
