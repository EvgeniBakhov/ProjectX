package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.EstateCreateRequest;
import com.projectx.ProjectX.model.resource.EstateUpdateResource;

public class EstateAssembler {

    public Estate fromCreateRequest(EstateCreateRequest request, User user) {
        Estate estate = new Estate();
        estate.setAddress(request.getAddress());
        estate.setArea(request.getArea());
        estate.setDescription(request.getDescription());
        estate.setNumOfBedrooms(request.getNumOfBedrooms());
        estate.setType(request.getType());
        estate.setRentPrice(request.getRentPrice());
        estate.setOwner(user);
        estate.setCreatedBy(user.getId().toString());
        estate.setModifiedBy(user.getId().toString());
        estate.getAddress().setCreatedBy(user.getId().toString());
        estate.getAddress().setModifiedBy(user.getId().toString());
        return estate;
    }

    public Estate fromUpdateResource(EstateUpdateResource resource, Estate estate) {
        estate.setNumOfBedrooms(resource.getNumOfBeds());
        estate.setArea(resource.getArea());
        estate.setType(resource.getType());
        estate.setRentPrice(resource.getRentPrice());
        estate.setDescription(resource.getDescription());
        estate.setModifiedBy("system");
        return estate;
    }
}
