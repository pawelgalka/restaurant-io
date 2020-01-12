package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.FeedbackEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

public class RaportEntity{

    private Long orderId;
    private FeedbackEnum serviceGrade;
    private FeedbackEnum bartenderGrade;
    private FeedbackEnum chefGrade;

    public RaportEntity(FeedbackEntity feedbackEntity){
        this.orderId = feedbackEntity.getId();
        this.serviceGrade = feedbackEntity.getServiceGrade();
        this.bartenderGrade = feedbackEntity.getBeverageGrade();
        this.chefGrade = feedbackEntity.getDishGrade();
    }

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

    public FeedbackEnum getBartenderGrade() {
        return bartenderGrade;
    }

    public void setBartenderGrade(FeedbackEnum bartenderGrade) {
        this.bartenderGrade = bartenderGrade;
    }

    public FeedbackEnum getChefGrade() {
        return chefGrade;
    }

    public void setChefGrade(FeedbackEnum chefGrade) {
        this.chefGrade = chefGrade;
    }
}
