package com.projectx.ProjectX.controller;

import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EstateCreateRequest;
import com.projectx.ProjectX.model.resource.EstateUpdateResource;
import com.projectx.ProjectX.service.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
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
    public ResponseEntity<Estate> updateEstateDetails(@RequestBody EstateUpdateResource resource,
                                                      @PathVariable Long estateId,
                                                      @AuthenticationPrincipal User user) {
        return ResponseEntity.of(estateService.updateEstateDetails(estateId, resource, user));
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
    public ResponseEntity<Void> deleteEstate(@PathVariable Long estateId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{estateId}")
    public ResponseEntity<Void> addPictures(@RequestParam("picture") MultipartFile picture) {
        return ResponseEntity.ok().build();
    }
}
