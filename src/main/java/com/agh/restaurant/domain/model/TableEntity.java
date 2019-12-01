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

    public Collection<ReservationEntity> getTableReservations() {
        return tableReservations;
    }

    public void setTableReservations(Collection<ReservationEntity> tableReservations) {
        this.tableReservations = tableReservations;
    }



    @Override
    public String toString() {
        return "TableEntity{" + "id=" + getId() +
                ", tableReservations=" + tableReservations.stream().map(x -> x.getId()) +
                '}';
    }
}
