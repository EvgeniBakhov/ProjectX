package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.EstateAssembler;
import com.projectx.ProjectX.assembler.EstateResponseAssembler;
import com.projectx.ProjectX.exceptions.EntityNotFoundException;
import com.projectx.ProjectX.exceptions.NotAllowedException;
import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EstateCreateRequest;
import com.projectx.ProjectX.model.resource.EstateResponseResource;
import com.projectx.ProjectX.model.resource.EstateUpdateResource;
import com.projectx.ProjectX.repository.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EstateService {

    private final String PICTURE_PATH = "/static/estate-pictures/";

    @Autowired
    EstateRepository estateRepository;

    @Autowired
    PictureService pictureService;

    @Autowired
    EstateResponseAssembler estateResponseAssembler;

    public EstateResponseResource publishEstate(EstateCreateRequest request, User owner) {
        EstateAssembler estateAssembler = new EstateAssembler();
        Estate estate;
        try {
            estate = estateAssembler.fromCreateRequest(request, owner);
            estate = estateRepository.save(estate);
        } catch (Exception e) {
            return null;
        }
        return estateResponseAssembler.fromEstate(estate);
    }

    public EstateResponseResource findEstateById(Long estateId) {
        EstateResponseResource estate = estateResponseAssembler.fromEstate(estateRepository.findById(estateId).get());
        return estate;
    }

    public Optional<List<Estate>> findEstatesByOwnerId(Long ownerId) {
        Optional<List<Estate>> estates;
        try {
            estates = estateRepository.findAllByOwner(ownerId);
        } catch (Exception e) {
            return Optional.empty();
        }
        return estates;
    }

    public EstateResponseResource updateEstateDetails(Long id,
                                                      EstateUpdateResource resource,
                                                      User principal) throws EntityNotFoundException, NotAllowedException {
        EstateAssembler estateAssembler = new EstateAssembler();
        Optional<Estate> existingEstate = estateRepository.findById(id);
        if (existingEstate.isPresent()) {
            if (existingEstate.get().getOwner().getId().equals(principal.getId())) {
                Estate updatedEstate = estateAssembler.fromUpdateResource(resource, existingEstate.get());
                return estateResponseAssembler.fromEstate(estateRepository.save(updatedEstate));
            } else {
                throw new NotAllowedException("User is not the owner of this estate.");
            }
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist");
        }
    }

    public Optional<List<Estate>> findEstatesByCity(String city) {
        return estateRepository.findAllByCity(city);
    }

    public void deleteEstate(Long estateId, User user) throws NotAllowedException, EntityNotFoundException {
        Optional<Estate> existingEstate = estateRepository.findById(estateId);
        if (existingEstate.isPresent()) {
            checkIfOwner(existingEstate.get(), user);
            existingEstate.get().setDeleted(true);
            estateRepository.save(existingEstate.get());
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
    }

    public void uploadPictures(Long estateId, MultipartFile[] pictures, User user)
            throws NotAllowedException, EntityNotFoundException, IOException {
        Optional<Estate> existingEstate = estateRepository.findById(estateId);
        if (existingEstate.isPresent()) {
            checkIfOwner(existingEstate.get(), user);
            String uploadDir = PICTURE_PATH + estateId + "/";
            Set<String> estatePictures = pictureService.persistPictures(uploadDir, pictures);
            existingEstate.get().setPictures(estatePictures);
            estateRepository.save(existingEstate.get());
        } else {
            throw new EntityNotFoundException("Estate with this id does not exist.");
        }
    }

    private void checkIfOwner(Estate existingEstate, User user) throws NotAllowedException {
        if (existingEstate.getOwner().getId().equals(user.getId())) {
            return;
        } else {
            throw new NotAllowedException("User is not the owner of this estate.");
        }
    }


}
