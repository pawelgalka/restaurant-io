package com.agh.restaurant.domain;

public enum FeedbackEnum {
    _5(5), _4(4), _3(3), _2(2), _1(1);

    private int grade;

    FeedbackEnum(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
