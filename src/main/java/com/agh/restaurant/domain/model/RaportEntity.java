/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.FeedbackEnum;

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
