/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.TableEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class TableResponse implements Serializable {

    private Long id;

    @JsonIgnoreProperties("tableReservation")
    private Collection<ReservationResponse> tableReservations;

    public TableResponse() {
        super();
    }

    public TableResponse(TableEntity x) {
        this.id = x.getId();
        this.tableReservations = x.getTableReservations() == null ? null : x.getTableReservations().stream().filter(y ->
                y.getTimeOfReservation().toLocalDate().equals(LocalDateTime.now().toLocalDate()) && Optional
                        .ofNullable(y.getOrderEntity())
                        .map(orderEntity -> !StageEnum.FINALIZED.equals(orderEntity.getStage())).orElse(true)
        ).map(ReservationResponse::new).sorted(Comparator.comparing(ReservationResponse::getTimeOfReservation)
        ).collect(
                Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Collection<ReservationResponse> getTableReservations() {
        return tableReservations;
    }

    public void setTableReservations(Collection<ReservationResponse> tableReservations) {
        this.tableReservations = tableReservations;
    }

    @Override public String toString() {
        return "TableResponse{" +
                "id=" + id +
                ", tableReservations=" + tableReservations +
                '}';
    }
}
