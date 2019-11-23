package com.agh.restaurant.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity(name = "reservation")
@Table(name = "RESERVATION")
public class ReservationEntity extends AbstractEntity{
    @Column(name = "CUSTOMER_NAME_")
    String customerName;

    @Column(name = "TIME_")
    LocalDateTime timeOfReservation;

    @Column(name = "TABLE_ID_")
    Integer tableId;

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getTimeOfReservation() {
        return timeOfReservation;
    }

    public Integer getTableId() {
        return tableId;
    }
}
