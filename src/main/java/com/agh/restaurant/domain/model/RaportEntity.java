package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.FeedbackEnum;

import javax.persistence.*;
import java.time.LocalDateTime;

public class RaportEntity extends AbstractEntity{

    @Column(name = "DATE_")
    LocalDateTime date;

    private Long orderId;
    private FeedbackEnum serviceGrade;
    private FeedbackEnum bartenderGrade;
    private FeedbackEnum chefGrade;

    public RaportEntity(FeedbackEntity feedbackEntity, LocalDateTime timeOfReservation){
        this.date = timeOfReservation;
        this.orderId = feedbackEntity.getId();
        this.serviceGrade = feedbackEntity.getServiceGrade();
        this.bartenderGrade = feedbackEntity.getBeverageGrade();
        this.chefGrade = feedbackEntity.getDishGrade();
    }


    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
