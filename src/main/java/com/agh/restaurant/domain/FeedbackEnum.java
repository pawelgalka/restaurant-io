package com.agh.restaurant.domain;

public enum FeedbackEnum {
    EXCELLENT(4), GOOD(3), MEDIUM(2), BAD(1);

    private int grade;
    FeedbackEnum(int grade){
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }
}
