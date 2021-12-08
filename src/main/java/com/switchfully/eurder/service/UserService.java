package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.user.User;
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

    public User createUser(User user) {
        validateUser(user);
        return userRepository.addUser(user);
    }

    public List<User> getUsers() {
        return userRepository.getUserList();
    }

    public User getUserBy(String userId) {
        return userRepository.getUserList().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new InvalidUserException("User with id: " + userId + " does not exist"));
    }

    private void validateUser(User user) {
        assertEmailNotInUse(user.getEmail());
        assertNullOrEmpty(user.getFirstName(), "firstname");
        assertNullOrEmpty(user.getLastName(), "lastname");
        assertNullOrEmpty(user.getEmail(), "email address");
        assertNullOrEmpty(user.getAddress(), "address");
        assertNullOrEmpty(user.getPhoneNumber(), "phone number");
    }

    private void assertEmailNotInUse(String email) {
        if (userRepository.getUserList().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new InvalidUserException("The email address is already in use.");
        }
    }

    private void assertNullOrEmpty(String toCheck, String fieldName) {
        if (toCheck == null || toCheck.trim().equals("")) {
            throw new InvalidUserException("The " + fieldName + " of the user is required.");
        }
    }
}
