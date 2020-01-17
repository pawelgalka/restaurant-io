/*
 * Copyright 2020 Pawel Galka
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.agh.restaurant.web.api;

import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.service.TableOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerApi {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TableOperationFacade tableOperationFacade;

    @PostMapping(value = "/reserve")
    public ResponseEntity createReservation(@RequestParam String customerName, @RequestParam
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime date, @RequestParam Integer duration) {
        ReservationEntity newReservation = new ReservationEntity();
        newReservation.setCustomerName(customerName);
        return createOrAlterReservation(date, duration, newReservation, null);

    }

    @DeleteMapping(value = "/reserve")
    public ResponseEntity deleteReservation(@RequestParam(required = false) String customerName,
            @RequestParam Long reservationId) {
        try {
            reservationRepository.deleteById(reservationId);
        } catch (EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE).body("unable to delete reservation.");
        }
        return ResponseEntity.ok().body("reservation deleted");

    }

    @PatchMapping(value = "/reserve")
    public ResponseEntity modifyReservation(@RequestParam(required = false) String customerName,
            @RequestParam Long reservationId, @RequestParam
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime date, @RequestParam(required = false) Integer duration) {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId).orElse(null);
        if (duration == null && reservationEntity != null){
            duration = reservationEntity.getDuration();
        }
        return createOrAlterReservation(date, duration, reservationEntity, reservationId);
    }

    private ResponseEntity createOrAlterReservation(LocalDateTime date, Integer duration,
            ReservationEntity newReservation, Long reservationId) {
        if (date.toLocalTime().isBefore(LocalTime.of(12, 0)) || date.toLocalTime().plusHours(duration).isAfter(LocalTime.MAX)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Restaurant opening hours exceeded. Please choose time slot between 12AM and 12PM.");
        }
        if (reservationId!=null){
            newReservation = reservationRepository.findById(reservationId).orElse(newReservation);
        }
        newReservation.setTimeOfReservation(date);
        newReservation.setDuration(duration);
        List<TableEntity> freeTables = tableOperationFacade.getTableFreeAtCertainTime(date, duration);

        if (freeTables.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No available table at certain time. No changes made to reservation.");
        }
        newReservation.setTableReservation(freeTables.get(0));
        reservationRepository.save(newReservation);
        return ResponseEntity.ok().body(newReservation.getId());
    }
}
