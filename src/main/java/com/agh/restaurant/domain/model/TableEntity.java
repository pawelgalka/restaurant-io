package com.agh.restaurant.domain.model;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "restaurant_table")
@Table(name = "RESTAURANT_TABLE")
public class TableEntity extends AbstractEntity {

    @OneToMany(mappedBy="tableReservation", fetch = FetchType.LAZY)
    private Collection<ReservationEntity> tableReservations;

    @OneToMany(mappedBy = "orderOfTable", fetch = FetchType.LAZY)
    private Collection<OrderEntity> orderEntities;
}
