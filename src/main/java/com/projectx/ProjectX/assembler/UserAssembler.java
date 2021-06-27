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
import java.util.Date;
import java.util.Optional;

@Component
public class UserAssembler {

    @Autowired
    RoleRepository roleRepository;

    public User fromUpdateResource(User user, UserUpdateResource resource) {
        user.setPhone(resource.getPhone() == null ? user.getPhone() : resource.getPhone());
        if (resource.getAddress() != null && user.getAddress().equals(resource.getAddress())) {
            user.getAddress().setRegion(resource.getAddress().getRegion() == null  ?
                    user.getAddress().getRegion() : resource.getAddress().getRegion());
            user.getAddress().setCountry(resource.getAddress().getCountry() == null  ?
                    user.getAddress().getCountry() : resource.getAddress().getCountry());
            user.getAddress().setSubdivision(resource.getAddress().getSubdivision() == null  ?
                    user.getAddress().getSubdivision() : resource.getAddress().getSubdivision());
            user.getAddress().setCity(resource.getAddress().getCity() == null  ?
                    user.getAddress().getCity() : resource.getAddress().getCity());
            user.getAddress().setStreet(resource.getAddress().getStreet() == null  ?
                    user.getAddress().getStreet() : resource.getAddress().getStreet());
            user.getAddress().setAdditionalDetails(resource.getAddress().getAdditionalDetails() == null  ?
                    user.getAddress().getAdditionalDetails() : resource.getAddress().getAdditionalDetails());
            user.getAddress().setModifiedBy(user.getId().toString());
            user.getAddress().setModifiedDate(new Date());
        }
        user.setFirstName(resource.getFirstName() == null ? user.getFirstName() : resource.getFirstName());
        user.setLastName(resource.getLastName() == null ? user.getLastName() : resource.getLastName());
        user.setEmail(resource.getEmail() == null ? user.getEmail() : resource.getEmail());
        user.setAge((resource.getAge() < 5 && resource.getAge() > 95) ? user.getAge() : resource.getAge());
        user.setModifiedBy(user.getId().toString());
        user.setModifiedDate(new Date());
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
        request.getAddress().setCreatedBy("system");
        request.getAddress().setModifiedBy("system");
        user.setAddress(request.getAddress());
        user.setPhone(request.getPhone());
        user.setEmailVerified(false);
        user.setType(request.getType());
        Optional<Role> role = roleRepository.findByName("ROLE_" + request.getType());
        user.setRoles(Arrays.asList(role.isPresent() ? role.get() : roleRepository.findByName("ROLE_NORMAL").get()));
        user.setCreatedBy("system");
        user.setModifiedBy("system");
        return user;
    }
}
