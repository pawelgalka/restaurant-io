package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
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
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@Secured(SecurityConfig.Roles.ROLE_CUSTOMER)
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
