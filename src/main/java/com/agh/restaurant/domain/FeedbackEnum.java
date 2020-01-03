package com.agh.restaurant.domain;

public enum FeedbackEnum {
    GREAT(5), GOOD(4), OKAY(3), BAD(2), TERRIBLE(1);

    private int grade;

    FeedbackEnum(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    protected void setGrade(int grade) {
        this.grade = grade;
    }
}
