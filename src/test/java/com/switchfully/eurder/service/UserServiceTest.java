package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.User;
import com.switchfully.eurder.domain.exceptions.InvalidUserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void givenAValidUser_whenAddingUserToRepository_thenUserIsSuccessfullyAdded() {
        // Given
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");

        // When
        userService.createUser(user);
        List<User> userList = userService.getUsers();

        // Then
        Assertions.assertThat(userList).contains(user);
    }

    @Test
    void givenAUserWithNoFirstName_whenAddingUserToRepository_thenInvalidUserExceptionIsThrown() {
        // Given
        User user = new User(null, "Voeten", "jordi@email.com", "Belgium", "01235");

        // When
        // Then
        Assertions.assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("The firstname of the user is required.");
    }

    @Test
    void givenAUserWithNoLastName_whenAddingUserToRepository_thenInvalidUserExceptionIsThrown() {
        // Given
        User user = new User("Jordi", null, "jordi@email.com", "Belgium", "01235");

        // When
        // Then
        Assertions.assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("The lastname of the user is required.");
    }

    @Test
    void givenAUserWithNoEmail_whenAddingUserToRepository_thenInvalidUserExceptionIsThrown() {
        // Given
        User user = new User("Jordi", "Voeten", null, "Belgium", "01235");

        // When
        // Then
        Assertions.assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("The email address of the user is required.");
    }

    @Test
    void givenAUserWithNoAddress_whenAddingUserToRepository_thenInvalidUserExceptionIsThrown() {
        // Given
        User user = new User("Jordi", "Voeten", "jordi@email.com", null, "01235");

        // When
        // Then
        Assertions.assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("The address of the user is required.");
    }

    @Test
    void givenAUserWithNoPhoneNumber_whenAddingUserToRepository_thenInvalidUserExceptionIsThrown() {
        // Given
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", null);

        // When
        // Then
        Assertions.assertThatThrownBy(() -> userService.createUser(user))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("The phone number of the user is required.");
    }
}