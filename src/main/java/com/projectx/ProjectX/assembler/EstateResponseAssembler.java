package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.model.Estate;
import com.projectx.ProjectX.model.resource.EstateResponseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EstateResponseAssembler {

    @Autowired
    UserResponseAssembler userResponseAssembler;

    public EstateResponseResource fromEstate(Estate estate) {
        EstateResponseResource estateResponseResource = new EstateResponseResource();
        estateResponseResource.setId(estate.getId());
        estateResponseResource.setAddress(estate.getAddress());
        estateResponseResource.setArea(estate.getArea());
        estateResponseResource.setRentPrice(estate.getRentPrice());
        estateResponseResource.setType(estate.getType());
        estateResponseResource.setDescription(estate.getDescription());
        estateResponseResource.setNumOfBedrooms(estate.getNumOfBedrooms());
        estateResponseResource.setPictures(estate.getPictures());
        estateResponseResource.setOwner(userResponseAssembler.fromUser(estate.getOwner()));
        return estateResponseResource;
    }

    public List<EstateResponseResource> fromEstateList(List<Estate> estates) {
        List<EstateResponseResource> resourceList = new ArrayList<>();
        estates.stream().forEach(estate -> resourceList.add(fromEstate(estate)));
        return resourceList;
    }
}
