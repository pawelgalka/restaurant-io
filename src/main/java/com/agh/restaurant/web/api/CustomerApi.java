package com.agh.restaurant.web.api;

import com.agh.restaurant.config.SecurityConfig;
import com.agh.restaurant.domain.dao.ReservationRepository;
import com.agh.restaurant.domain.model.ReservationEntity;
import com.agh.restaurant.domain.model.TableEntity;
import com.agh.restaurant.service.TableOperationFacade;
import org.apache.log4j.Logger;
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

    private static Logger logger = Logger.getLogger(ManagerApi.class);

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    TableOperationFacade tableOperationFacade;

    @PostMapping(value = "/reserve")
    public ResponseEntity createReservation(@RequestParam String customerName, @RequestParam
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime date) {
        ReservationEntity newReservation = new ReservationEntity();
        newReservation.setCustomerName(customerName);
        return createOrAlterReservation(date, newReservation);

    }

    @DeleteMapping(value = "/reserve")
    public ResponseEntity deleteReservation(@RequestParam(required = false) String customerName,
            @RequestParam Long reservationId) {
        reservationRepository.delete(reservationId);
        return ResponseEntity.ok().body("reservation deleted");

    }

    @PatchMapping(value = "/reserve")
    public ResponseEntity modifyReservation(@RequestParam(required = false) String customerName,
            @RequestParam Long reservationId, @RequestParam
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime date) {
        ReservationEntity reservationEntity = reservationRepository.findOne(reservationId);
        reservationRepository.delete(reservationId);
        return createOrAlterReservation(date, reservationEntity);
    }

    private ResponseEntity createOrAlterReservation(LocalDateTime date, ReservationEntity newReservation) {
        newReservation.setTimeOfReservation(date);
        List<TableEntity> freeTables = tableOperationFacade.getTableFreeAtCertainTime(date);

        if (freeTables.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("No available table at certain time");
        }

        newReservation.setTableReservation(freeTables.get(0));
        reservationRepository.save(newReservation);
        logger.info("saved reservation");
        return ResponseEntity.ok().body(newReservation.getId());
    }
}
