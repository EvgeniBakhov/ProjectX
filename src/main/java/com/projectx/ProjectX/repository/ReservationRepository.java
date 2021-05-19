package com.projectx.ProjectX.repository;

import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.Reservation;
import com.projectx.ProjectX.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(Long reservationId);

    Optional<List<Reservation>> getAllByEvent(Event event);

    Optional<List<Reservation>> getAllByUser(User user);

    Optional<Reservation> getByEventAndUser(Event event, User user);
}
