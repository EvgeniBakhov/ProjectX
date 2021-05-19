package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EstateCreateRequest;
import com.projectx.ProjectX.model.resource.EstateResponseResource;
import com.projectx.ProjectX.model.resource.EstateUpdateResource;
import com.projectx.ProjectX.service.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/estate")
public class EstateController {

    @Autowired
    EstateService estateService;

    @PreAuthorize("hasAuthority('PUBLISH_ESTATE')")
    @PostMapping()
    public ResponseEntity<Void> publishEstate(@RequestBody EstateCreateRequest request,
                                              @AuthenticationPrincipal User user) {
        if(estateService.publishEstate(request, user)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping(value = "/{estateId}")
    public ResponseEntity updateEstateDetails(@RequestBody EstateUpdateResource resource,
                                                      @PathVariable Long estateId,
                                                      @AuthenticationPrincipal User user) {
        EstateResponseResource updatedEstate = null;
        try {
            updatedEstate = estateService.updateEstateDetails(estateId, resource, user);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (NotAllowedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.ok(updatedEstate);
    }

    @GetMapping("/{estateId}")
    public ResponseEntity<Estate> getEstateById(@PathVariable Long estateId) {
        return ResponseEntity.of(estateService.findEstateById(estateId));
    }

    @GetMapping()
    public ResponseEntity<List<Estate>> getEstatesByUserId(@PathParam("owner") Long ownerId) {
        return ResponseEntity.of(estateService.findEstatesByOwnerId(ownerId));
    }

    @GetMapping("/?city=")
    public ResponseEntity<List<Estate>> getEstatesByCity(@PathParam("city") String city) {
        Optional<List<Estate>> estates = estateService.findEstatesByCity(city);
        return ResponseEntity.of(estates);
    }

    @DeleteMapping("/{estateId}")
    public ResponseEntity deleteEstate(@PathVariable Long estateId, @AuthenticationPrincipal User user) {
        try {
            estateService.deleteEstate(estateId, user);
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{estateId}")
    public ResponseEntity uploadPictures(@RequestParam("pictures") MultipartFile[] pictures,
                                               @PathVariable Long estateId, @AuthenticationPrincipal User user) {
        try {
            estateService.uploadPictures(estateId, pictures, user);
        } catch (NotAllowedException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error peristing files");
        }
        return ResponseEntity.ok().build();
    }
}
