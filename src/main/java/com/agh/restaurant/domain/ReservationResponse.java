package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.ReservationEntity;

import java.time.LocalDateTime;

public class ReservationResponse {
    private Long id;
    private Integer duration;

    private String customerName;

    private LocalDateTime timeOfReservation;

    private OrderResponse orderEntity;

    public ReservationResponse(ReservationEntity y) {
        this.customerName = y.getCustomerName();
        this.id = y.getId();
        this.timeOfReservation = y.getTimeOfReservation();
        this.orderEntity = y.getOrderEntity() == null ? null : new OrderResponse(y.getOrderEntity());
        this.duration = y.getDuration();

    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getTimeOfReservation() {
        return timeOfReservation;
    }

    public void setTimeOfReservation(LocalDateTime timeOfReservation) {
        this.timeOfReservation = timeOfReservation;
    }

    public OrderResponse getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderResponse orderEntity) {
        this.orderEntity = orderEntity;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
