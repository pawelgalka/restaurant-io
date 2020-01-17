/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
