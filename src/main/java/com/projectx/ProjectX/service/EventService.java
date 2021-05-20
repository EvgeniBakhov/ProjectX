package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.EventAssembler;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.Picture;
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
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final String PICTURE_PATH = "/static/event-pictures/";


    @Autowired
    EventRepository eventRepository;

    @Autowired
    PictureService pictureService;

    @Autowired
    EventResponseAssembler eventResponseAssembler;

    public boolean createEvent(EventCreateRequest request, User user) {
        EventAssembler eventAssembler = new EventAssembler();
        Event event = eventAssembler.fromCreateRequest(request, user);
        try {
            if (validateEvent(event)) {
                eventRepository.save(event);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void updateEvent(Long eventId, EventUpdateRequest request, User user) throws EntityNotFoundException, NotAllowedException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        EventAssembler eventAssembler = new EventAssembler();
        if (existingEvent.isPresent()) {
            if (existingEvent.get().getOrganizer().getId().equals(user.getId())) {
                Event updatedEvent = eventAssembler.fromUpdateRequest(request, existingEvent.get());
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
        return event.getCapacity() < 1000;
    }

    public void uploadPictures(Long eventId, MultipartFile[] pictures, User user)
            throws NotAllowedException, EntityNotFoundException, IOException {
        Optional<Event> existingEvent = eventRepository.findById(eventId);
        if (existingEvent.isPresent()) {
            checkIfOrganizer(existingEvent.get(), user);
            String uploadDir = PICTURE_PATH + eventId + "/";
            List<Picture> estatePictures = pictureService.persistPictures(uploadDir, pictures);
            existingEvent.get().setPictures(estatePictures);
            eventRepository.save(existingEvent.get());
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
    }

    public boolean checkIfOrganizer(Event event, User user) throws NotAllowedException {
        if (event.getOrganizer().getId().equals(user.getId())) {
            return true;
        } else {
            throw new NotAllowedException("You are not the organizer of this event.");
        }
    }
}
