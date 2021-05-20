package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.ReservationResponseResource;
import com.projectx.ProjectX.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping(value = "/{reservationId}/cancel")
    public ResponseEntity cancelReservation(@PathVariable Long reservationId, @AuthenticationPrincipal User user) {
        try {
            reservationService.cancelReservation(reservationId, user);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping(value = "/{reservationId}/approve")
    public ResponseEntity approveReservation(@PathVariable Long reservationId, @AuthenticationPrincipal User user) {
        try {
            reservationService.approveReservation(reservationId, user);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/{reservationId}")
    public ResponseEntity getReservationById(@PathVariable Long reservationId, @AuthenticationPrincipal User user) {
        try {
            ReservationResponseResource reservation = reservationService.findReservationById(reservationId, user);
            return ResponseEntity.ok().body(reservation);
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity findReservationsForCurrentUser(@AuthenticationPrincipal User user) {
        List<ReservationResponseResource> reservations = reservationService.findReservationsForCurrentUser(user);
        return ResponseEntity.ok().body(reservations);
    }

    @GetMapping(value = "/event/{eventId}")
    public ResponseEntity findReservationsForEvent(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        try {
            List<ReservationResponseResource> reservations = reservationService.findReservationsForEvent(eventId, user);
            return ResponseEntity.ok().body(reservations);
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(value = "user/{userId}")
    public ResponseEntity findReservationsByUserId(@PathVariable Long userId, @AuthenticationPrincipal User user) {
        try {
            List<ReservationResponseResource> reservations = reservationService.findReservationsByUserId(userId, user);
            return ResponseEntity.ok().body(reservations);
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
