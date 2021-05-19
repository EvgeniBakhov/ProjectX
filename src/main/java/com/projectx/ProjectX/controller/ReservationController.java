package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.ReservationResponseResource;
import com.projectx.ProjectX.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/reservation")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @PostMapping(value = "/{eventId}")
    public ResponseEntity createReservation(@PathVariable Long eventId,
                                            @AuthenticationPrincipal User user) {
        try {
            ReservationResponseResource reservation = reservationService.createReservation(eventId, user);
            return ResponseEntity.ok().body(reservation);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{eventId}/cancel")
    public ResponseEntity cancelReservation() {
        return null;
    }

    @PutMapping(value = "/{eventId}/approve")
    public ResponseEntity approveReservation() {
        return null;
    }

    @GetMapping(value = "/{reservationId}")
    public ResponseEntity getReservationById() {
        return null;
    }

    @GetMapping()
    public ResponseEntity findReservationsForCurrentUser(@AuthenticationPrincipal User user) {
        return null;
    }

    @GetMapping(value = "/event/{eventId}")
    public ResponseEntity findReservationForEvent(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        return null;
    }

    @GetMapping(value = "user/{userId}")
    public ResponseEntity findReservationsByUserId(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        return null;
    }
}
