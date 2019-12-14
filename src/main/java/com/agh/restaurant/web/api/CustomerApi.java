package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.domain.ReservationCustomer;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.service.TableOperationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

    @GetMapping("/cos")
    public String c(){
        return "COS";
    }
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
        reservationRepository.deleteById(reservationId);
        return ResponseEntity.ok().body("reservation deleted");

    }

    @PatchMapping(value = "/reserve")
    public ResponseEntity modifyReservation(@RequestParam(required = false) String customerName,
            @RequestParam Long reservationId, @RequestParam
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime date, @RequestParam(required = false) Integer duration) {
        ReservationEntity reservationEntity = reservationRepository.findById(reservationId).orElse(null);
        Integer currentDuration = reservationEntity.getDuration();
        if (duration == null){
            duration = reservationEntity.getDuration();
        }
        return createOrAlterReservation(date, duration, reservationEntity, reservationId);
    }

    private ResponseEntity createOrAlterReservation(LocalDateTime date, Integer duration,
            ReservationEntity newReservation, Long reservationId) {
        if (date.toLocalTime().isBefore(LocalTime.of(12, 0)) || date.toLocalTime().plusHours(duration).isAfter(LocalTime.MAX)){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Restaurant opening hours exceeded. Please choose time slot between 12AM and 12PM.");
        }
        newReservation.setTimeOfReservation(date);
        newReservation.setDuration(duration);
        List<TableEntity> freeTables = tableOperationFacade.getTableFreeAtCertainTime(date, duration);

        if (freeTables.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No available table at certain time. No changes made to reservation.");
        }
        if (reservationId!= null){
            reservationRepository.deleteById(reservationId);
        }
        newReservation.setTableReservation(freeTables.get(0));
        reservationRepository.save(newReservation);
        return ResponseEntity.ok().body(newReservation.getId());
    }
}
