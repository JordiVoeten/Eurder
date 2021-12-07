package com.switchfully.eurder.service;

import com.switchfully.eurder.domain.user.User;
import com.switchfully.eurder.domain.exceptions.InvalidUserException;
import com.switchfully.eurder.domain.user.UserType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setup() {
        user1 = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");
        user2 = new User("Jordi2", "Voeten", "jordi2@email.com", "Belgium", "01235");
        user3 = new User("Jordi3", "Voeten", "jordi3@email.com", "Belgium", "01235");
        userService.createUser(user2);
        userService.createUser(user3);
    }

    @Test
    void givenAUserListWhichWeFill_whenGettingTheList_thenTheListShouldBeCorrectlyFilled() {
        // Given
        List<User> validUserList = new ArrayList<>(userService.getUsers());
        validUserList.add(user1);
        validUserList.add(user2);
        validUserList.add(user3);
        userService.createUser(user1);
        // When
        List<User> userList = userService.getUsers();

        // Then
        Assertions.assertThat(userList).containsAll(validUserList);
    }

    @Test
    void givenAUsers_whenTryingToCreateUserWithSameEmail_thenInvalidUserException() {
        // Given
        userService.createUser(user1);
        // Then
        Assertions.assertThatThrownBy(() -> userService.createUser(user1))
                .isInstanceOf(InvalidUserException.class)
                .hasMessage("The email address is already in use.");
    }

    @Test
    void givenAValidUser_whenAddingUserToRepository_thenUserIsSuccessfullyAdded() {
        // Given
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");

        // When
        User created = userService.createUser(user);
        User found = userService.getUserBy(user.getId());

        // Then
        Assertions.assertThat(created).isEqualTo(found);
        Assertions.assertThat(created.getUserType()).isEqualTo(UserType.CUSTOMER);
        Assertions.assertThat(created.getUserType().getUserValue()).isEqualTo(UserType.CUSTOMER.getUserValue());
    }

    @Test
    void givenAnAdmin_whenGettingThatAdmin_thenCheckUserType() {
        // Given

        // When
        User found = userService.getUsers().stream()
                .filter(user -> user.getEmail().contains("admin@mail.com"))
                .findAny().get();

        // Then
        Assertions.assertThat(found.getUserType()).isEqualTo(UserType.ADMIN);
        Assertions.assertThat(found.getUserType().getUserValue()).isEqualTo(UserType.ADMIN.getUserValue());
    }

    @Test
    void givenAValidUser_whenAddingUserToRepository_thenUserIsCustomer() {
        // Given
        User user = new User("Jordi", "Voeten", "jordi@email.com", "Belgium", "01235");

        // When
        userService.createUser(user);
        User createdUser = userService.getUserBy(user.getId());

        // Then
        Assertions.assertThat(createdUser.getUserType()).isEqualTo(UserType.CUSTOMER);
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