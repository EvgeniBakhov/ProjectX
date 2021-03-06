package com.projectx.ProjectX.repository;

import com.projectx.ProjectX.enums.BookingStatus;
import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<List<Booking>> getAllByToDateBetweenAndStatus(Date fromDate, Date toDate, BookingStatus status);

    Optional<List<Booking>> getAllByFromDateBetweenAndStatus(Date fromDate, Date toDate, BookingStatus status);

    @Query("FROM Booking b WHERE " +
            "(b.toDate BETWEEN :fromDate AND :toDate) " +
            "AND " +
            "(b.fromDate BETWEEN :fromDate AND :toDate) " +
            "AND " +
            "b.status = :status")
    Optional<List<Booking>> getAllByFromAndToDateAreBetweenAndStatus(Date fromDate, Date toDate, BookingStatus status);

    @Query("FROM Booking b WHERE " +
            "b.fromDate < :fromDate " +
            "AND " +
            "b.toDate > :toDate " +
            "AND " +
            "b.status = :status")
    Optional<List<Booking>> getAllByFromDateIsLessAndToDateIsMore(Date fromDate, Date toDate, BookingStatus status);

    Optional<List<Booking>> getAllByEstate(Estate estate);

    Optional<List<Booking>> getAllByUser(User user);

    Optional<List<Booking>> getAllByEstateAndStatus(Estate estate, BookingStatus status);

    @Query("FROM Booking b WHERE " +
            "b.estate = :estate " +
            "AND " +
            "(b.fromDate BETWEEN :fromDate AND :toDate) " +
            "OR " +
            "(b.toDate BETWEEN :fromDate AND :toDate)")
    Optional<List<Booking>> getAllByEstateAndFromDateIsBetweenOrToDateIsBetween(Estate estate, Date fromDate, Date toDate);

    Optional<List<Booking>> getAllByEstateAndFromDateIsGreaterThan(Estate estate, Date date);
}
