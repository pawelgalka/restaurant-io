package com.agh.restaurant.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "restaurant_table")
@Table(name = "RESTAURANT_TABLE")
public class TableEntity extends AbstractEntity {

    @OneToMany(mappedBy="tableReservation", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tableReservation")
    private Collection<ReservationEntity> tableReservations;

    @OneToMany(mappedBy = "orderOfTable", fetch = FetchType.LAZY)
    private Collection<OrderEntity> orderEntities;

    @ManyToMany(cascade = { CascadeType.ALL })
    private Collection<UserEntity> waiterEntities;

    public Collection<ReservationEntity> getTableReservations() {
        return tableReservations;
    }

    public void setTableReservations(Collection<ReservationEntity> tableReservations) {
        this.tableReservations = tableReservations;
    }

    public Collection<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(Collection<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public Collection<UserEntity> getWaiterEntities() {
        return waiterEntities;
    }

    public void setWaiterEntities(Collection<UserEntity> waiterEntities) {
        this.waiterEntities = waiterEntities;
    }

    @Override
    public String toString() {
        return "TableEntity{" + "id=" + getId() +
                ", tableReservations=" + tableReservations.stream().map(x -> x.getId()) +
                ", orderEntities=" + orderEntities +
                '}';
    }
}
