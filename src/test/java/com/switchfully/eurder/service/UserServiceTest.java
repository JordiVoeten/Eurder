package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.user.User;
import com.switchfully.eurder.domain.exceptions.InvalidUserException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void givenAUserListWhichWeFill_whenGettingTheList_thenTheListShouldBeCorrectlyFilled() {
        // Given
        User user1 = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        User user2 = new User("Jordi2", "Voeten", "jordi@email.com", "Belgium", "01235");
        User user3 = new User("Jordi3", "Voeten", "jordi@email.com", "Belgium", "01235");
        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        List<User> validUserList = List.of(user1, user2, user3);

        // When
        List<User> userList = userService.getUsers();

        // Then
        Assertions.assertThat(userList).isEqualTo(validUserList);
    }

    @Test
    void givenAEmptyUserList_whenGettingTheList_thenTheListShouldBeCorrectlyFilled() {
        // Given
        List<User> validUserList = new ArrayList<>();

        // When
        List<User> userList = userService.getUsers();

        // Then
        Assertions.assertThat(userList).isEqualTo(validUserList);
    }

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

    @Test
    void givenAValidUserId_whenGettingUserById_thenGetUserByThatId() {
        // Given
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        User valid = userService.createUser(user);
        // When
        User found = userService.getUserBy(user.getId());

        // Then
        Assertions.assertThat(found).isEqualTo(valid);
    }
    @Test
    void givenAValidUserWithIdNotInRepository_whenGettingUserById_thenInvalidUserException() {
        // Given
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "number");

        // When
        // Then
        Assertions.assertThatThrownBy(() -> userService.getUserBy(user.getId()))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("User with id: " + user.getId() + " does not exist");
    }
}