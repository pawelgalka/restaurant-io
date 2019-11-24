package com.agh.restaurant.domain.model;

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
}
