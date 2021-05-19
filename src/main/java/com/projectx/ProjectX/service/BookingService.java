package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.BookingAssembler;
import com.projectx.ProjectX.assembler.BookingResponseAssembler;
import com.projectx.ProjectX.assembler.UserResponseAssembler;
import com.projectx.ProjectX.enums.BookingStatus;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.InvalidBookingException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
import com.projectx.ProjectX.model.Booking;
import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.BookingRequest;
import com.projectx.ProjectX.model.resource.BookingResponseResource;
import com.projectx.ProjectX.model.resource.UpdateBookingRequest;
import com.projectx.ProjectX.repository.BookingRepository;
import com.projectx.ProjectX.repository.EstateRepository;
import com.projectx.ProjectX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    EstateRepository estateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingAssembler bookingAssembler;

    @Autowired
    BookingResponseAssembler bookingResponseAssembler;

    public Booking bookEstate(Long estateId, User user, BookingRequest bookingRequest) throws InvalidBookingException, EntityNotFoundException {
        Optional<Estate> existingEstate = estateRepository.findById(estateId);
        if (!existingEstate.isPresent()) {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
        Booking booking = bookingAssembler.fromBookingRequest(bookingRequest, existingEstate.get(), user);
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

    public void cancelBooking(Long bookingId, User user) throws EntityNotFoundException, NotAllowedException {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isPresent()) {
            if (existingBooking.get().getUser().getId().equals(user.getId())
                    || existingBooking.get().getEstate().getOwner().getId().equals(user.getId())) {
                existingBooking.get().setStatus(BookingStatus.CANCELLED);
                existingBooking.get().setModifiedDate(new Date());
                existingBooking.get().setModifiedBy(user.getId().toString());
                bookingRepository.save(existingBooking.get());
            } else {
                throw new NotAllowedException("You have no rights to update this booking.");
            }
        } else {
            throw new EntityNotFoundException("Booking with this id does not exist.");
        }
    }

    public void approveBooking(Long bookingId, User user) throws EntityNotFoundException, NotAllowedException {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isPresent()) {
            if ((existingBooking.get().getUser().getId().equals(user.getId())
                    || existingBooking.get().getEstate().getOwner().getId().equals(user.getId()))
                    && BookingStatus.CREATED.equals(existingBooking.get().getStatus())) {
                existingBooking.get().setStatus(BookingStatus.APPROVED);
                existingBooking.get().setModifiedBy(user.getId().toString());
                existingBooking.get().setModifiedDate(new Date());
                bookingRepository.save(existingBooking.get());
            } else {
                throw new NotAllowedException("You have no rights to update this booking.");
            }
        } else {
            throw new EntityNotFoundException("Booking with this id does not exist.");
        }
    }

    public List<BookingResponseResource> findBookingsForEstate(Long estateId, User user)
            throws NotAllowedException, EntityNotFoundException {
        Optional<Estate> estate = estateRepository.findById(estateId);
        if (estate.isPresent()) {
            if (estate.get().getOwner().getId().equals(user.getId())) {
                Optional<List<Booking>> estateBookings = bookingRepository.getAllByEstate(estate.get());
                return bookingResponseAssembler.fromBookingsList(estateBookings.get());
            } else {
                throw new NotAllowedException("You have to be an owner to view this info.");
            }
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
    }

    public List<BookingResponseResource> findRelevantBookingsForEstate(Long estateId, User user)
            throws NotAllowedException, EntityNotFoundException {
        Optional<Estate> estate = estateRepository.findById(estateId);
        if (estate.isPresent()) {
            if (estate.get().getOwner().getId().equals(user.getId())) {
                Date today = new Date();
                Optional<List<Booking>> bookings =
                        bookingRepository.getAllByEstateAndFromDateIsGreaterThan(estate.get(), today);
                return bookingResponseAssembler.fromBookingsList(bookings.get());
            } else {
                throw new NotAllowedException("You have to be an owner to view this info.");
            }
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
    }

    public List<BookingResponseResource> findBookingsForEstateWithStatus(Long estateId, User user, BookingStatus status)
            throws NotAllowedException, EntityNotFoundException {
        Optional<Estate> estate = estateRepository.findById(estateId);
        if (estate.isPresent()) {
            if (estate.get().getOwner().getId().equals(user.getId())) {
                Optional<List<Booking>> estateBookings = bookingRepository
                        .getAllByEstateAndStatus(estate.get(), status);
                return bookingResponseAssembler.fromBookingsList(estateBookings.get());
            } else {
                throw new NotAllowedException("You have to be an owner to view this info.");
            }
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
    }

    public List<BookingResponseResource> findBookingsBetweenDates(Long estateId, Date fromDate, Date toDate, User user)
            throws EntityNotFoundException, InvalidBookingException, NotAllowedException {
        if(validateDates(fromDate, toDate)) {
            Optional<Estate> estate = estateRepository.findById(estateId);
            if (estate.isPresent()) {
                if (estate.get().getOwner().getId().equals(user.getId())) {
                    Optional<List<Booking>> bookings = bookingRepository
                            .getAllByEstateAndFromDateIsBetweenOrToDateIsBetween(estate.get(), fromDate, toDate);
                    return bookingResponseAssembler.fromBookingsList(bookings.get());
                } else {
                    throw new NotAllowedException("You must be owner of this estate to view this info.");
                }
            } else {
                throw new EntityNotFoundException("Estate with this id does not exist.");
            }
        } else {
            throw new InvalidBookingException("Invalid dates!");
        }
    }

    public BookingResponseResource findById(Long bookingId, User user)
            throws EntityNotFoundException, NotAllowedException {
        Optional<Booking> existingBooking = bookingRepository.findById(bookingId);
        if (existingBooking.isPresent()) {
            if (existingBooking.get().getUser().getId().equals(user.getId())
                    || existingBooking.get().getEstate().getOwner().getId().equals(user.getId())) {
                return bookingResponseAssembler.fromBooking(existingBooking.get());
            } else {
                throw new NotAllowedException("You cannot view this booking.");
            }
        } else {
            throw new EntityNotFoundException("Booking with this id does not exist.");
        }
    }

    public Optional<List<BookingResponseResource>> findForCurrentUser(User user) {
        Optional<List<Booking>> userBookings = bookingRepository.getAllByUser(user);
        if (userBookings.isPresent()) {
            return Optional.of(bookingResponseAssembler.fromBookingsList(userBookings.get()));
        }
        return Optional.empty();
    }

    public List<BookingResponseResource> findForUser(Long userId, User user) throws EntityNotFoundException {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isPresent()) {
            Optional<List<Booking>> userBookings = bookingRepository.getAllByUser(existingUser.get());
            return bookingResponseAssembler.fromBookingsList(userBookings.get());
        } else {
            throw new EntityNotFoundException("User with this id does not exist.");
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
        return validateDates(booking.getFromDate(), booking.getToDate());
    }

    private boolean validateDates(Date fromDate, Date toDate) {
        return toDate.compareTo(fromDate) > 0 && fromDate.compareTo(new Date()) >= 0;
    }
}
