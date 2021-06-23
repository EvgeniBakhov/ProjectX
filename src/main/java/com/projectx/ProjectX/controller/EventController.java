package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
import com.projectx.ProjectX.model.Event;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EventCreateRequest;
import com.projectx.ProjectX.model.resource.EventResponseResource;
import com.projectx.ProjectX.model.resource.EventUpdateRequest;
import com.projectx.ProjectX.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/event")
public class EventController {

    @Autowired
    EventService eventService;

    @PreAuthorize("hasAuthority('PUBLISH_EVENT')")
    @PostMapping()
    public ResponseEntity createEvent(@RequestBody EventCreateRequest request, @AuthenticationPrincipal User user) {
        EventResponseResource response = eventService.createEvent(request, user);
        if (response == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/{eventId}")
    public ResponseEntity updateEvent(@PathVariable Long eventId,
                                             @RequestBody EventUpdateRequest request,
                                             @AuthenticationPrincipal User user) {
        try {
            eventService.updateEvent(eventId, request, user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{eventId}")
    public ResponseEntity deleteEvent(@PathVariable Long eventId, @AuthenticationPrincipal User user) {
        try {
            eventService.deleteEvent(eventId, user);
        } catch (NotAllowedException e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{eventId}")
    public ResponseEntity findEventById(@PathVariable Long eventId) {
        EventResponseResource event = null;
        try {
            event = eventService.findEventById(eventId);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().body(event);
    }
    @PostMapping("/{eventId}")
    public ResponseEntity uploadPictures(@RequestParam("pictures") MultipartFile[] pictures,
                                         @PathVariable Long eventId, @AuthenticationPrincipal User user) {
        try {
            eventService.uploadPictures(eventId, pictures, user);
            return ResponseEntity.ok().build();
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error persisting files");
        }
    }

    @PostMapping("/{eventId}/thumbnail")
    public ResponseEntity uploadThumbnail(@RequestParam("thumbnail") MultipartFile thumbnail,
                                         @PathVariable Long eventId, @AuthenticationPrincipal User user) {
        try {
            eventService.uploadThumbnail(eventId, thumbnail, user);
            return ResponseEntity.ok().build();
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error persisting files");
        }
    }
}
