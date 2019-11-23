package com.agh.restaurant.domain.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reservation")
@Table(name = "RESERVATION")
public class ReservationEntity extends AbstractEntity{
    @Column(name = "CUSTOMER_NAME_")
    String customerName;

    @Column(name = "TIME_")
    LocalDateTime timeOfReservation;

    @ManyToOne
    @JoinColumn(name="TABLE_ID_")
    private TableEntity tableReservation;

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getTimeOfReservation() {
        return timeOfReservation;
    }

}
