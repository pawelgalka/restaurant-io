package com.agh.restaurant.domain;

public class FeedbackPojo {
    private FeedbackEnum serviceGrade;
    private FeedbackEnum beverageGrade;
    private FeedbackEnum dishGrade;

    public FeedbackPojo(FeedbackEnum serviceGrade, FeedbackEnum beverageGrade,
            FeedbackEnum dishGrade) {
        this.serviceGrade = serviceGrade;
        this.beverageGrade = beverageGrade;
        this.dishGrade = dishGrade;
    }

    public FeedbackPojo() {
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
