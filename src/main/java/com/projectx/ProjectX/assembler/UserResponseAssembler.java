package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.UserResponseResource;
import org.springframework.stereotype.Component;

@Component
public class UserResponseAssembler {

    public UserResponseResource fromUser(User user) {
        UserResponseResource resource = new UserResponseResource();
        resource.setUsername(user.getUsername());
        resource.setFirstName(user.getFirstName());
        resource.setLastName(user.getLastName());
        resource.setAge(user.getAge());
        resource.setEmail(user.getEmail());
        resource.setPhone(user.getPhone());
        resource.setAddress(user.getAddress());
        resource.setPicture(user.getPicture());
        resource.setType(user.getType());
        return resource;
    }
}
