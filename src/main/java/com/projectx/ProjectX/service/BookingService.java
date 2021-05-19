package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.BookingAssembler;
import com.projectx.ProjectX.enums.BookingStatus;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.InvalidBookingException;
import com.projectx.ProjectX.exceptions.UpdateNotAllowedException;
import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.BookingRequest;
import com.projectx.ProjectX.model.resource.UpdateBookingRequest;
import com.projectx.ProjectX.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    BookingAssembler bookingAssembler;

    public Booking bookEstate(User user, BookingRequest bookingRequest) throws InvalidBookingException {
        Booking booking = bookingAssembler.fromBookingRequest(bookingRequest, user);
        if (validateBooking(booking)) {
            if (!checkForApprovedBookings(booking)) {
                booking = bookingRepository.save(booking);
            } else {
                throw new InvalidBookingException("Estate is booked for this date");
            }
        } else {
            throw new InvalidBookingException("fromDate must be before the toDate");
        }
        return booking;
    }

    public void updateBooking(Long bookingId, User user, UpdateBookingRequest request)
            throws InvalidBookingException, EntityNotFoundException {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isPresent()) {
            Booking updatedBooking = bookingAssembler.fromUpdateRequest(request, existingBooking.get());
            if (validateBooking(updatedBooking)) {
                if (!checkForApprovedBookings(updatedBooking)) {
                    bookingRepository.save(updatedBooking);
                } else {
                    throw new InvalidBookingException("Estate is booked for this date.");
                }
            } else {
                throw new InvalidBookingException("fromDate nust be before the toDate");
            }
        } else {
            throw new EntityNotFoundException("Booking with this id does not exist.");
        }
    }

    public void cancelBooking(Long bookingId, User user) throws EntityNotFoundException, UpdateNotAllowedException {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isPresent()) {
            if (existingBooking.get().getUser().getId().equals(user.getId())
                    || existingBooking.get().getEstate().getOwner().getId().equals(user.getId())) {
                existingBooking.get().setStatus(BookingStatus.CANCELLED);
                bookingRepository.save(existingBooking.get());
            } else {
                throw new UpdateNotAllowedException("You have no rights to update this booking.");
            }
        } else {
            throw new EntityNotFoundException("Booking with this id does not exist.");
        }
    }

    private boolean checkForApprovedBookings(Booking booking) {
        Optional<List<Booking>> fromDateCrossing = bookingRepository
                .getAllByFromDateBetweenAndStatus(booking.getFromDate(), booking.getToDate(), BookingStatus.APPROVED);
        Optional<List<Booking>> toDateCrossing = bookingRepository
                .getAllByToDateBetweenAndStatus(booking.getFromDate(), booking.getToDate(), BookingStatus.APPROVED);
        Optional<List<Booking>> bookingBetweenFromAndToDate = bookingRepository
                .getAllByFromAndToDateAreBetweenAndStatus(booking.getFromDate(), booking.getToDate(), BookingStatus.APPROVED);
        Optional<List<Booking>> bookingIsInExistingBooking = bookingRepository
                .getAllByFromDateIsLessAndToDateIsMore(booking.getFromDate(), booking.getToDate(), BookingStatus.APPROVED);
        return (fromDateCrossing.isPresent() && !fromDateCrossing.get().isEmpty())
                || (toDateCrossing.isPresent() && !toDateCrossing.get().isEmpty())
                || (bookingBetweenFromAndToDate.isPresent() && !bookingBetweenFromAndToDate.get().isEmpty())
                || (bookingIsInExistingBooking.isPresent() && !bookingIsInExistingBooking.get().isEmpty());
    }

    private boolean validateBooking(Booking booking) {
        return booking.getToDate().compareTo(booking.getFromDate()) > 0
                && (booking.getFromDate().compareTo(new Date())) > 0;
    }

    public void approveBooking(Long bookingId, User user) {

    }

    public void findBookingsForEstate(Long estateId, User user) {

    }

    public void findRelevantBookingsForEstate(Long estateId, User user) {

    }

    public void findApprovedBookingsForEstate(Long estateId, User user) {

    }

    public void findBookingsBetweenDates(Long estateId, Date fromDate, Date toDate, User user) {

    }

    public void findById(Long bookingId, User user) {

    }

    public void findForCurrentUser(User user) {

    }

    public void findForUser(Long userId, User user) {

    }
}
