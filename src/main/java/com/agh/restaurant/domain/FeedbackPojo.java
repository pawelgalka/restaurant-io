package com.agh.restaurant.domain;

public class FeedbackPojo {
    private FeedbackEnum serviceGrade;
    private FeedbackEnum beverageGrade;
    private FeedbackEnum dishGrade;
    private Long orderId;

    public FeedbackPojo(FeedbackEnum serviceGrade, FeedbackEnum beverageGrade,
            FeedbackEnum dishGrade, Long orderId) {
        this.serviceGrade = serviceGrade;
        this.beverageGrade = beverageGrade;
        this.dishGrade = dishGrade;
        this.orderId = orderId;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setServiceGrade(FeedbackEnum serviceGrade) {
        this.serviceGrade = serviceGrade;
    }

    public void setBeverageGrade(FeedbackEnum beverageGrade) {
        this.beverageGrade = beverageGrade;
    }

    public void setDishGrade(FeedbackEnum dishGrade) {
        this.dishGrade = dishGrade;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
