package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.OrderEntity;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

public class ReservationResponse {
    private final Long id;
    private String customerName;

    private LocalDateTime timeOfReservation;

    private TableEntity tableReservation;

    private OrderResponse orderEntity;

    public ReservationResponse(ReservationEntity y) {
        this.customerName = y.getCustomerName();
        this.id = y.getId();
        this.tableReservation = y.getTableReservation();
        this.timeOfReservation = y.getTimeOfReservation();
        this.orderEntity = y.getOrderEntity() == null ? null : new OrderResponse(y.getOrderEntity());

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

    public TableEntity getTableReservation() {
        return tableReservation;
    }

    public void setTableReservation(TableEntity tableReservation) {
        this.tableReservation = tableReservation;
    }

    public OrderResponse getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderResponse orderEntity) {
        this.orderEntity = orderEntity;
    }
}
