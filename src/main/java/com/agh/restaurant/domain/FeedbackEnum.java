package com.agh.restaurant.domain;

public enum FeedbackEnum {
    GRADE_5(5), GRADE_4(4), GRADE_3(3), GRADE_2(2), GRADE_1(1);

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
