package com.agh.restaurant.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

@Entity(name = "restaurant_table")
@Table(name = "RESTAURANT_TABLE")
public class TableEntity extends AbstractEntity implements Serializable {

    @OneToMany(mappedBy="tableReservation", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("tableReservation")
    private Collection<ReservationEntity> tableReservations;

    public Collection<ReservationEntity> getTableReservations() {
        return tableReservations;
    }

    public void setTableReservations(Collection<ReservationEntity> tableReservations) {
        this.tableReservations = tableReservations;
    }

    public TableEntity withId(Long id){
        this.setId(id);
        return this;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TableEntity))
            return false;
        if (!super.equals(o))
            return false;
        TableEntity that = (TableEntity) o;
        return Objects.equals(getTableReservations(), that.getTableReservations());
    }

    @Override public int hashCode() {
        return Objects.hash(super.hashCode(), getTableReservations());
    }

    @Override public String toString() {
        return "TableEntity{" +
                "tableReservations=" + tableReservations +
                '}';
    }

    public TableEntity withTableReservations(Collection<ReservationEntity> objects) {
        this.setTableReservations(objects);
        return this;
    }
}
