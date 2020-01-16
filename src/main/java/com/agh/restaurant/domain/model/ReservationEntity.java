package com.agh.restaurant.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity(name = "reservation")
@Table(name = "RESERVATION")
public class ReservationEntity extends AbstractEntity implements Serializable {
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
    @JsonIgnoreProperties("reservationEntity")
    private OrderEntity orderEntity;

    @Column(name = "DURATION_")
    private Integer duration;

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

    public ReservationEntity withOrderEntity(OrderEntity orderEntity){
        this.setOrderEntity(orderEntity);
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

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ReservationEntity withDate(LocalDateTime localDateTime) {
        this.setTimeOfReservation(localDateTime);
        return this;
    }

    public ReservationEntity withDuration(Integer duration){
        this.setDuration(duration);
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ReservationEntity))
            return false;
        if (!super.equals(o))
            return false;
        ReservationEntity that = (ReservationEntity) o;
        return Objects.equals(getCustomerName(), that.getCustomerName()) &&
                Objects.equals(getTimeOfReservation(), that.getTimeOfReservation()) &&
                Objects.equals(getTableReservation(), that.getTableReservation()) &&
                Objects.equals(getOrderEntity(), that.getOrderEntity()) &&
                Objects.equals(getDuration(), that.getDuration());
    }

    @Override public int hashCode() {
        return Objects
                .hash(super.hashCode(), getCustomerName(), getTimeOfReservation(), getTableReservation(),
                        getOrderEntity(),
                        getDuration());
    }
}
