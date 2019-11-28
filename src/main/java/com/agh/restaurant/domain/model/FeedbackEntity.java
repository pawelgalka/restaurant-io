package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.FeedbackEnum;

import javax.persistence.*;

@Entity(name = "feedback")
@Table(name = "FEEDBACK")
public class FeedbackEntity extends AbstractEntity{

    public FeedbackEntity(Long orderId, FeedbackEnum serviceGrade, FeedbackEnum beverageGrade, FeedbackEnum dishGrade) {
        this.serviceGrade = serviceGrade;
        this.beverageGrade = beverageGrade;
        this.dishGrade = dishGrade;
        this.orderId = orderId;
    }

    public FeedbackEntity() {
    }

    @Column(name = "ORDER_ID_")
    private Long orderId;

    @Column(name = "SERVICE_GRADE_")
    FeedbackEnum serviceGrade;

    @Column(name = "BEVERAGE_GRADE_")
    FeedbackEnum beverageGrade;

    @Column(name = "DISH_GRADE_")
    FeedbackEnum dishGrade;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

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
}
