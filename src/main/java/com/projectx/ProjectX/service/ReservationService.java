package com.projectx.ProjectX.service;

import com.projectx.ProjectX.enums.EventStatus;
import com.projectx.ProjectX.enums.ReservationStatus;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.Reservation;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.ReservationResponseResource;
import com.projectx.ProjectX.repository.EventRepository;
import com.projectx.ProjectX.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    EventRepository eventRepository;

    public ReservationResponseResource createReservation(Long eventId, User user) throws EntityNotFoundException {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            if(checkEventStatus(event.get())
                    && checkExistingReservation(event.get(), user)
                    && checkPlaces(event.get())
                    && checkEsrbRate(user, event.get())) {
                Reservation reservation = getDefaultReservation(user, event.get());
                event.get().setAvailableSeats(event.get().getAvailableSeats() - 1);
                eventRepository.save(event.get());
                reservationRepository.save(reservation);
            }
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
        return null;
    }

    private boolean checkPlaces(Event event) {
        return event.getAvailableSeats() >= 1;
    }

    private boolean checkEsrbRate(User user, Event event) {
        return user.getAge() > event.getAgeRestrictions().getValue();
    }

    private boolean checkEventStatus(Event event) {
        return EventStatus.RIGHT_NOW.equals(event.getStatus()) || EventStatus.CANCELLED.equals(event.getStatus());
    }

    private boolean checkExistingReservation(Event event, User user) {
        Optional<Reservation> reservation = reservationRepository.getByEventAndUser(event, user);
        return !reservation.isPresent();
    }

    private Reservation getDefaultReservation(User user, Event event) {
        Reservation reservation = new Reservation();
        reservation.setEvent(event);
        reservation.setUser(user);
        reservation.setStatus(ReservationStatus.CREATED);
        reservation.setCreatedBy(user.getId().toString());
        reservation.setCreatedDate(new Date());
        reservation.setModifiedBy(user.getId().toString());
        reservation.setModifiedDate(new Date());
        return reservation;
    }
}
