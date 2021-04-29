package com.projectx.ProjectX.service;

import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.repository.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstateService {

    @Autowired
    EstateRepository estateRepository;

    public boolean publishEstate(Estate estate) {
        try {
            estateRepository.save(estate);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Optional<Estate> findEstateById(Long estateId) {
        Optional<Estate> estate;
        try {
            estate = estateRepository.findById(estateId);
        } catch (Exception e) {
            return Optional.empty();
        }
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
}
