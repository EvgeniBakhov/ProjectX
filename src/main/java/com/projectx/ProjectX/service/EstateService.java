package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.EstateAssembler;
import com.projectx.ProjectX.assembler.EstateResponseAssembler;
import com.projectx.ProjectX.exceptions.EstateNotFoundException;
import com.projectx.ProjectX.exceptions.UpdateNotAllowedException;
import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EstateCreateRequest;
import com.projectx.ProjectX.model.resource.EstateResponseResource;
import com.projectx.ProjectX.model.resource.EstateUpdateResource;
import com.projectx.ProjectX.repository.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstateService {

    @Autowired
    EstateRepository estateRepository;

    @Autowired
    EstateResponseAssembler estateResponseAssembler;

    public boolean publishEstate(EstateCreateRequest request, User owner) {
        EstateAssembler estateAssembler = new EstateAssembler();
        try {
            Estate estate = estateAssembler.fromCreateRequest(request, owner);
            estateRepository.save(estate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Optional<Estate> findEstateById(Long estateId) {
        return estateRepository.findById(estateId);
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
                                                                User principal) throws EstateNotFoundException, UpdateNotAllowedException {
        EstateAssembler estateAssembler = new EstateAssembler();
        Optional<Estate> existingEstate = estateRepository.findById(id);
        if (existingEstate.isPresent()) {
            if (existingEstate.get().getOwner().getId().equals(principal.getId())) {
                Estate updatedEstate = estateAssembler.fromUpdateResource(resource, existingEstate.get());
                return estateResponseAssembler.fromEstate(estateRepository.save(updatedEstate));
            } else {
                throw new UpdateNotAllowedException("User is not the owner of this estate.");
            }
        } else {
            throw new EstateNotFoundException("Estate with this id does not exist");
        }
    }

    public Optional<List<Estate>> findEstatesByCity(String city) {
        return estateRepository.findAllByCity(city);
    }

    public void deleteEstate(Long estateId, User user) throws UpdateNotAllowedException, EstateNotFoundException {
        Optional<Estate> existingEstate = estateRepository.findById(estateId);
        if (existingEstate.isPresent()) {
            if (existingEstate.get().getOwner().getId().equals(user.getId())) {
                existingEstate.get().setDeleted(true);
                estateRepository.save(existingEstate.get());
            } else {
                throw new UpdateNotAllowedException("User is not the owner of this estate.");
            }
        } else {
            throw new EstateNotFoundException("Estate with this id does not exist.");
        }
    }
}
