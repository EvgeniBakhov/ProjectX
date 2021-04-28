package com.projectx.ProjectX.service;

import com.projectx.ProjectX.model.User;
import com.projectx.ProjectX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void register(User user) {
        userRepository.save(user);
    }
}
