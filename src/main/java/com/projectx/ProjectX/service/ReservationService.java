package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.ReservationResponseAssembler;
import com.projectx.ProjectX.enums.EventStatus;
import com.projectx.ProjectX.enums.ReservationStatus;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.Reservation;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.ReservationResponseResource;
import com.projectx.ProjectX.repository.EventRepository;
import com.projectx.ProjectX.repository.ReservationRepository;
import com.projectx.ProjectX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationResponseAssembler reservationResponseAssembler;

    public ReservationResponseResource createReservation(Long eventId, User user) throws EntityNotFoundException, NotAllowedException {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            if(checkEventStatus(event.get())
                    && checkExistingReservation(event.get(), user)
                    && checkPlaces(event.get())
                    && checkEsrbRate(user, event.get())) {
                Reservation reservation = getDefaultReservation(user, event.get());
                reservation = reservationRepository.save(reservation);
                return reservationResponseAssembler.fromReservation(reservation);
            } else {
                throw new NotAllowedException("Not allowed reservation. Maybe you have one already.");
            }
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
    }

    private boolean checkPlaces(Event event) {
        return event.getAvailableSeats() > 0;
    }

    private boolean checkEsrbRate(User user, Event event) {
        return user.getAge() >= event.getAgeRestrictions().getValue();
    }

    private boolean checkEventStatus(Event event) {
        return !(EventStatus.RIGHT_NOW.equals(event.getStatus()) && EventStatus.CANCELLED.equals(event.getStatus()));
    }

    private boolean checkExistingReservation(Event event, User user) {
        Optional<Reservation> reservation = reservationRepository.getByEventAndUser(event, user);
        return !reservation.isPresent() || reservation.get().getStatus().equals(ReservationStatus.CANCELLED);
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

    public List<ReservationResponseResource> findReservationsForCurrentUser(User user) {
        Optional<List<Reservation>> reservations = reservationRepository.getAllByUser(user);
        return reservationResponseAssembler.fromReservationList(reservations.get());
    }

    public boolean approveReservation(Long reservationId, User user)
            throws EntityNotFoundException, NotAllowedException {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            Event event = reservation.get().getEvent();
            if (event.getOrganizer().getId().equals(user.getId())
                    || !ReservationStatus.CANCELLED.equals(reservation.get().getStatus())) {
                reservation.get().setStatus(ReservationStatus.APPROVED);
                event.setAvailableSeats(event.getAvailableSeats() - 1);
                eventRepository.save(event);
                reservationRepository.save(reservation.get());
                return true;
            } else {
                throw new NotAllowedException("You do not have rights to approve this reservation");
            }
        } else {
            throw new EntityNotFoundException("Reservation with this id does not exist.");
        }
    }

    public boolean cancelReservation(Long reservationId, User user) throws NotAllowedException, EntityNotFoundException {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            if (reservation.get().getEvent().getOrganizer().getId().equals(user.getId())
                    || reservation.get().getUser().getId().equals(user.getId())) {
                if (reservation.get().getStatus().equals(ReservationStatus.APPROVED)) {
                    Event event = reservation.get().getEvent();
                    event.setAvailableSeats(event.getAvailableSeats() + 1);
                    eventRepository.save(event);
                }
                reservation.get().setStatus(ReservationStatus.CANCELLED);
                reservationRepository.save(reservation.get());
                return true;
            } else {
                throw new NotAllowedException("You do not have rights to cancel the reservation.");
            }
        } else {
            throw new EntityNotFoundException("Reservation with this id does not exist.");
        }
    }

    public ReservationResponseResource findReservationById(Long reservationId, User user) throws NotAllowedException, EntityNotFoundException {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        if (reservation.isPresent()) {
            if(reservation.get().getUser().equals(user.getId())
                    || reservation.get().getEvent().getOrganizer().getId().equals(user.getId())) {
                return reservationResponseAssembler.fromReservation(reservation.get());
            } else {
                throw new NotAllowedException("You don't have rights to view this information.");
            }
        } else {
            throw new EntityNotFoundException("Reservation with this id does not exist.");
        }
    }

    public List<ReservationResponseResource> findReservationsForEvent(Long eventId, User user) throws NotAllowedException, EntityNotFoundException {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            if (event.get().getOrganizer().getId().equals(user.getId())) {
                Optional<List<Reservation>> reservations = reservationRepository.getAllByEvent(event.get());
                return reservationResponseAssembler.fromReservationList(reservations.get());
            } else {
                throw new NotAllowedException("You don't have rights to view this information.");
            }
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
    }

    public List<ReservationResponseResource> findReservationsByUserId(Long userId, User user) throws NotAllowedException, EntityNotFoundException {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            if(user.getAuthorities().stream()
                    .filter(grantedAuthority -> grantedAuthority.equals("EDIT_USER")).toArray().length > 0) {
                Optional<List<Reservation>> userReservations = reservationRepository.getAllByUser(existingUser.get());
                return reservationResponseAssembler.fromReservationList(userReservations.get());
            } else {
                throw new NotAllowedException("You don't have rights to view this information.");
            }
        } else {
            throw new EntityNotFoundException("User with this id does not exist.");
        }
    }
}
