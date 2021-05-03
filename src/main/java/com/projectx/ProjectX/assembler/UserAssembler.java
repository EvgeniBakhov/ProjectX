package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.UserResponseResource;
import com.projectx.ProjectX.model.resource.UserUpdateResource;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    public User fromUpdateResource(User user, UserUpdateResource resource) {
        user.setPhone(resource.getPhone() == null ? user.getPhone() : resource.getPhone());
        user.setAddress(resource.getAddress() == null ? user.getAddress() : resource.getAddress());
        user.setPicture(resource.getPicture() == null ? user.getPicture() : resource.getPicture());
        return user;
    }
}
