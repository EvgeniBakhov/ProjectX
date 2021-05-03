package com.projectx.ProjectX.service;

import com.projectx.ProjectX.assembler.UserAssembler;
import com.projectx.ProjectX.assembler.UserResponseAssembler;
import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.model.resource.UserResponseResource;
import com.projectx.ProjectX.model.resource.UserUpdateResource;
import com.projectx.ProjectX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:" +
            "\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|" +
            "\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" +
            "\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))" +
            "\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c" +
            "\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAssembler userAssembler;

    @Autowired
    UserResponseAssembler userResponseAssembler;

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()
                || userRepository.findByEmail(user.getEmail()).isPresent()
                || !validateEmail(user.getEmail())) {
            return null;
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateUserProfile(User principal, UserUpdateResource resource) {
        Optional<User> existingUser = userRepository.findById(principal.getId());
        if (existingUser.isPresent()) {
            User updatedUser = userAssembler.fromUpdateResource(principal, resource);
            userRepository.save(updatedUser);
        }
    }

    public UserResponseResource findUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() ? null : userResponseAssembler.fromUser(user.get());
    }

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
