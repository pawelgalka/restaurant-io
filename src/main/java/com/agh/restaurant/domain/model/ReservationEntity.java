package com.agh.restaurant.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reservation")
@Table(name = "RESERVATION")
public class ReservationEntity extends AbstractEntity{
    @Column(name = "CUSTOMER_NAME_")
    private
    String customerName;

    @Column(name = "TIME_")
    private
    LocalDateTime timeOfReservation;

    @ManyToOne
    @JoinColumn(name="TABLE_ID_")
    private TableEntity tableReservation;

    @OneToOne
    @JoinColumn(name="ORDER_ID_")
    private OrderEntity orderEntity;

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

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public ReservationEntity withTable(TableEntity table){
        this.setTableReservation(table);
        return this;
    }

    @Override public String toString() {
        return "ReservationEntity{" +
                "customerName='" + customerName + '\'' +
                ", timeOfReservation=" + timeOfReservation +
                ", tableReservation=" + tableReservation +
                ", orderEntity=" + orderEntity +
                '}';
    }
}
