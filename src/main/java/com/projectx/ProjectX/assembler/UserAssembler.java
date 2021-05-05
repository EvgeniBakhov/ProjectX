package com.projectx.ProjectX.assembler;

import com.projectx.ProjectX.model.Role;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.UserRegistrationRequest;
import com.projectx.ProjectX.model.resource.UserUpdateResource;
import com.projectx.ProjectX.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class UserAssembler {

    @Autowired
    RoleRepository roleRepository;

    public User fromUpdateResource(User user, UserUpdateResource resource) {
        user.setPhone(resource.getPhone() == null ? user.getPhone() : resource.getPhone());
        user.setAddress(resource.getAddress() == null ? user.getAddress() : resource.getAddress());
        user.setPicture(resource.getPicture() == null ? user.getPicture() : resource.getPicture());
        return user;
    }

    public User fromRegistrationRequest(UserRegistrationRequest request) {
        User user = new User();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setEmailVerified(false);
        Optional<Role> role = roleRepository.findByName("ROLE_" + request.getType());
        user.setRoles(Arrays.asList(role.isPresent() ? role.get() : roleRepository.findByName("ROLE_NORMAL").get()));
        user.setCreatedBy("system");
        user.setModifiedBy("system");
        return user;
    }
}
