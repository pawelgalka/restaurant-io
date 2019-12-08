package com.agh.restaurant.domain;

import com.agh.restaurant.domain.model.TableEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TableResponse {

    private Long id;

    @JsonIgnoreProperties("tableReservation")
    private Collection<ReservationResponse> tableReservations;

    public TableResponse(TableEntity x) {
        this.id = x.getId();
        this.tableReservations = x.getTableReservations() == null ? null : x.getTableReservations().stream().filter(y ->
            y.getTimeOfReservation().toLocalDate().equals(LocalDateTime.now().toLocalDate())
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
}
