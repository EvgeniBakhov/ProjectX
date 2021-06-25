package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.EventAssembler;
import com.projectx.ProjectX.enums.EventPlaceType;
import com.projectx.ProjectX.enums.EventStatus;
import com.projectx.ProjectX.enums.EventType;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EventCreateRequest;
import com.projectx.ProjectX.assembler.EventResponseAssembler;
import com.projectx.ProjectX.model.resource.EventResponseResource;
import com.projectx.ProjectX.model.resource.EventUpdateRequest;
import com.projectx.ProjectX.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final String PICTURE_PATH = "/static/event-pictures/";


    @Autowired
    EventRepository eventRepository;

    @Autowired
    PictureService pictureService;

    @Autowired
    EventResponseAssembler eventResponseAssembler;

    public EventResponseResource createEvent(EventCreateRequest request, User user) throws Exception {
        EventAssembler eventAssembler = new EventAssembler();
        Event event = eventAssembler.fromCreateRequest(request, user);
        try {
            if (validateEvent(event)) {
                 event = eventRepository.save(event);
            } else {
                throw new NotAllowedException("Some of the fields are not valid.");
            }
        } catch (Exception e) {
            throw new Exception("Event creation failed due to unkhown error");
        }
        return eventResponseAssembler.fromEvent(event);
    }

    public void updateEvent(Long eventId, EventUpdateRequest request, User user) throws EntityNotFoundException, NotAllowedException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            if (existingEvent.get().getOrganizer().getId().equals(user.getId())) {
                Event updatedEvent = createUpdateEvent(request, existingEvent.get());
                if (updatedEvent == null) {
                    throw new NotAllowedException("Update values not valid.");
                }
                eventRepository.save(updatedEvent);
            } else {
                throw new NotAllowedException("You are not the organizer of this event.");
            }
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
    }

    public void deleteEvent(Long eventId, User user) throws NotAllowedException, EntityNotFoundException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            if (existingEvent.get().getOrganizer().getId().equals(user.getId())) {
                existingEvent.get().setDeleted(true);
                eventRepository.save(existingEvent.get());
            } else {
                throw new NotAllowedException("You are not the organizer of this event.");
            }
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
    }


    public EventResponseResource findEventById(Long eventId) throws EntityNotFoundException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            return eventResponseAssembler.fromEvent(existingEvent.get());
        } else {
            throw new EntityNotFoundException("Event with this id does not exist.");
        }
    }

    private boolean validateEvent(Event event) {
        return event.getCapacity() < 1000
                && event.getStartDate().before(event.getEndDate())
                && event.getStartDate().after(new Date());
    }

    public void uploadPictures(Long eventId, MultipartFile[] pictures, User user)
            throws NotAllowedException, EntityNotFoundException, IOException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            checkIfOrganizer(existingEvent.get(), user);
            String uploadDir = PICTURE_PATH + eventId + "/";
            Set<String> estatePictures = pictureService.persistPictures(uploadDir, pictures);
            existingEvent.get().setPictures(estatePictures);
            eventRepository.save(existingEvent.get());
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
    }

    private boolean checkIfOrganizer(Event event, User user) throws NotAllowedException {
        if (event.getOrganizer().getId().equals(user.getId())) {
            return true;
        } else {
            throw new NotAllowedException("You are not the organizer of this event.");
        }
    }

    public void uploadThumbnail(Long eventId, MultipartFile thumbnail, User user)
            throws NotAllowedException, IOException, EntityNotFoundException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            checkIfOrganizer(existingEvent.get(), user);
            String uploadDir = PICTURE_PATH + eventId + "/";
            Set<String> savedThumbnail = pictureService
                    .persistPictures(uploadDir, (MultipartFile[]) Arrays.asList(thumbnail).toArray());
            existingEvent.get().setThumbnail(savedThumbnail.stream().findFirst().get());
            eventRepository.save(existingEvent.get());
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
    }

    private Event createUpdateEvent(EventUpdateRequest request, Event event) {
        if (event.getStartDate().after(request.getStartDate())) {
            return null;
        }
        EventAssembler eventAssembler = new EventAssembler();
        return eventAssembler.fromUpdateRequest(request, event);
    }

    public EventResponseResource findTopEvent(User user) {
        List<Event> events = eventRepository.findAll(user.getAddress().getCity(), "", "", EventStatus.PLANNED.toString(), "", "");
        return eventResponseAssembler.fromEvent(extractTop(events));
    }

    private Event extractTop(List<Event> events) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date nextWeek = calendar.getTime();
        events = events.stream().filter(event -> event.getStartDate().before(nextWeek)).collect(Collectors.toList());
        return events.size() == 0 ? null : events.get((int)(Math.random()*(events.size()-1)));
    }

    public List<EventResponseResource> findAllWithFilters(String city, String type, String placeType, String status, String availableSeats, String ageRestrictions) {
        List<Event> events = eventRepository.findAll(
                city,
                type,
                placeType,
                status, ageRestrictions, availableSeats);
        if (events.size() > 0) {
            return eventResponseAssembler.fromEventList(events);
        }
        return null;
    }
}
