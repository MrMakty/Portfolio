package com.spaceinvaders.spaceinvadersbackend.controller;

import com.spaceinvaders.spaceinvadersbackend.dao.ReservationDAO;
import com.spaceinvaders.spaceinvadersbackend.dto.ExtensionDTO;
import com.spaceinvaders.spaceinvadersbackend.dto.ReservationDTO;
import com.spaceinvaders.spaceinvadersbackend.models.Reservation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@EnableMethodSecurity(prePostEnabled = true)
public class ReservationController {

    private final ReservationDAO reservationDAO;

    public ReservationController(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @PostMapping()
    public ResponseEntity<Reservation> createReservation(@RequestBody @Valid ReservationDTO reservationDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationDAO.createReservation(reservationDTO));
    }

    @GetMapping()
    public ResponseEntity<List<Reservation>> getCurrentReservationsByUser() {
        return ResponseEntity.ok(reservationDAO.getAllCurrentReservationsByUser());
    }


    @GetMapping("/active")
    public ResponseEntity<List<Reservation>> getActiveReservations() {
        return ResponseEntity.ok(reservationDAO.getActiveReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationDAO.getReservationById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> extendReservation(
            @PathVariable Long id,
            @RequestBody @Valid ExtensionDTO extensionDTO) {
        return ResponseEntity.ok(reservationDAO.extendReservation(id, extensionDTO));
    }

    @DeleteMapping("/myreservation/{id}")
    public ResponseEntity<Reservation> deleteReservation(@PathVariable Long id) {
        reservationDAO.cancelMyReservation(id);
        return ResponseEntity.ok().build();
    };

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> cancelAnyReservationAdmin(@PathVariable Long id) {
        reservationDAO.cancelReservation(id);
        return ResponseEntity.ok().build();
    }
}
