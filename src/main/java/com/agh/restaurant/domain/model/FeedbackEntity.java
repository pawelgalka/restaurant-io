package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.FeedbackEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "feedback")
@Table(name = "FEEDBACK")
public class FeedbackEntity extends AbstractEntity{

    public FeedbackEntity(Long waiterId, Long bartenderId, Long chefId, FeedbackEnum serviceGrade, FeedbackEnum beverageGrade, FeedbackEnum dishGrade) {
        this.waiterId = waiterId;
        this.bartenderId = bartenderId;
        this.chefId = chefId;
        this.serviceGrade = serviceGrade;
        this.beverageGrade = beverageGrade;
        this.dishGrade = dishGrade;
    }

    public FeedbackEntity() {
    }

    @Column(name = "WAITER_ID_")
    Long waiterId;

    @Column(name = "BARTENDER_ID_")
    Long bartenderId;

    @Column(name = "CHEF_ID_")
    Long chefId;

    @Column(name = "SERVICE_GRADE_")
    FeedbackEnum serviceGrade;

    @Column(name = "BEVERAGE_GRADE_")
    FeedbackEnum beverageGrade;

    @Column(name = "DISH_GRADE_")
    FeedbackEnum dishGrade;

    public Long getWaiterId() {
        return waiterId;
    }

    public Long getBartenderId() {
        return bartenderId;
    }

    public Long getChefId() {
        return chefId;
    }

    public FeedbackEnum getServiceGrade() {
        return serviceGrade;
    }

    public FeedbackEnum getBeverageGrade() {
        return beverageGrade;
    }

    public FeedbackEnum getDishGrade() {
        return dishGrade;
    }
}
