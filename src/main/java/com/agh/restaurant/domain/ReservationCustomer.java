package com.agh.restaurant.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class ReservationCustomer {
    Long id;
    String customerName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime date;
    Integer duration;

    public ReservationCustomer() {
    }

    public ReservationCustomer(Long id, String customerName, LocalDateTime date, Integer duration) {
        this.id = id;
        this.customerName = customerName;
        this.date = date;
        this.duration = duration;
    }

    public ReservationCustomer(String customerName, LocalDateTime date, Integer duration) {
        this.customerName = customerName;
        this.date = date;
        this.duration = duration;
    }

    public ReservationCustomer(Long id, String customerName) {
        this.id = id;
        this.customerName = customerName;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
