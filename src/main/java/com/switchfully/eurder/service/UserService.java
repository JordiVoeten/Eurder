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

    public User createUser(User newUser) {
        validateUser(newUser);
        return userRepository.addUser(newUser);
    }

    public List<User> getUsers() {
        return userRepository.getUserList();
    }

    public User getUserBy(String userId){
        return userRepository.getUserList().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new InvalidUserException("User with id: " + userId + " does not exist"));
    }

    private void validateUser(User newUser) {
        assertNullOrEmpty(newUser.getFirstName(), "firstname");
        assertNullOrEmpty(newUser.getLastName(), "lastname");
        assertNullOrEmpty(newUser.getEmail(), "email address");
        assertNullOrEmpty(newUser.getAddress(), "address");
        assertNullOrEmpty(newUser.getPhoneNumber(), "phone number");
    }

    private void assertNullOrEmpty(String toCheck, String fieldName) {
        if (toCheck == null || toCheck.trim().equals("")) {
            throw new InvalidUserException("The " + fieldName + " of the user is required.");
        }
    }

}
