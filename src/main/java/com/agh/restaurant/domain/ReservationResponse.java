package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.ReservationEntity;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ReservationResponse implements Serializable {
    private Long id;
    private Integer duration;

    private String customerName;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timeOfReservation;

    private OrderResponse orderEntity;

    public ReservationResponse(){super();}

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

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    @Override public String toString() {
        return "ReservationResponse{" +
                "id=" + id +
                ", duration=" + duration +
                ", customerName='" + customerName + '\'' +
                ", timeOfReservation=" + timeOfReservation +
                ", orderEntity=" + orderEntity +
                '}';
    }
}
