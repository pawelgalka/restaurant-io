package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.FeedbackEnum;

import javax.persistence.*;

@Entity(name = "feedback")
@Table(name = "FEEDBACK")
public class FeedbackEntity extends AbstractEntity{

    public FeedbackEntity(Long waiterId, Long bartenderId, Long chefId, FeedbackEnum serviceGrade, FeedbackEnum beverageGrade, FeedbackEnum dishGrade) {
        this.serviceGrade = serviceGrade;
        this.beverageGrade = beverageGrade;
        this.dishGrade = dishGrade;
    }

    public FeedbackEntity() {
    }

    @ManyToOne
    @JoinColumn(name="WAITER_ID_")
    private UserEntity waiter;

    @ManyToOne
    @JoinColumn(name="BARTENDER_ID_")
    UserEntity bartender;

    @ManyToOne
    @JoinColumn(name="CHEF_ID_")
    UserEntity chef;


    @Column(name = "SERVICE_GRADE_")
    FeedbackEnum serviceGrade;

    @Column(name = "BEVERAGE_GRADE_")
    FeedbackEnum beverageGrade;

    @Column(name = "DISH_GRADE_")
    FeedbackEnum dishGrade;

    public UserEntity getWaiter() {
        return waiter;
    }

    public UserEntity getBartender() {
        return bartender;
    }

    public UserEntity getChef() {
        return chef;
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
