package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.User;
import com.switchfully.eurder.domain.exceptions.InvalidUserException;
import com.switchfully.eurder.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User newUser) {
        validateUser(newUser);
        User user = userRepository.addUser(newUser);
        return user;
    }

    public List<User> getUsers() {
        return userRepository.getUserList();
    }

    private void validateUser(User newUser) {
        if (newUser.getFirstName() == null || newUser.getFirstName().trim().equals("")) {
            throw new InvalidUserException("The firstname of the user is required.");
        }
        if (newUser.getLastName() == null || newUser.getLastName().trim().equals("")) {
            throw new InvalidUserException("The lastname of the user is required.");
        }
        if (newUser.getEmail() == null || newUser.getEmail().trim().equals("")) {
            throw new InvalidUserException("The email address of the user is required.");
        }
        if (newUser.getAddress() == null || newUser.getAddress().trim().equals("")) {
            throw new InvalidUserException("The address of the user is required.");
        }
        if (newUser.getPhoneNumber() == null || newUser.getPhoneNumber().trim().equals("")) {
            throw new InvalidUserException("The phone number of the user is required.");
        }
    }

}
