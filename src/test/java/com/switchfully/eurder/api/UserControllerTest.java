package com.switchfully.eurder.api;

import com.switchfully.eurder.domain.user.dto.CreateUserDto;
import com.switchfully.eurder.domain.user.dto.UserDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Value("${server.port}")
    protected int port;
    private String admin;

    @BeforeEach
    void setup() {
        admin = "Basic " + Base64.getEncoder().encodeToString("admin@hotmail.com:".getBytes(StandardCharsets.UTF_8));
    }

    @Nested
    @DisplayName("Creating a user test")
    class CreateAUserTest {

        @Test
        void givenValidCreateUserDto_whenCreateUser_thenUserDtoGetsReturnedCorrectly() {
            // Given
            CreateUserDto createUserDto = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");

            // When
            UserDto userDto =
                    RestAssured
                            .given()
                            .body(createUserDto)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.CREATED.value())
                            .extract()
                            .as(UserDto.class);

            Assertions.assertThat(userDto.getFirstName()).isEqualTo(createUserDto.getFirstName());
            Assertions.assertThat(userDto.getLastName()).isEqualTo(createUserDto.getLastName());
            Assertions.assertThat(userDto.getAddress()).isEqualTo(createUserDto.getAddress());
            Assertions.assertThat(userDto.getEmail()).isEqualTo(createUserDto.getEmail());
            Assertions.assertThat(userDto.getPhoneNumber()).isEqualTo(createUserDto.getPhoneNumber());
        }

        @Test
        void givenValidCreateUserDtoOfAUserThatAlreadyExists_whenCreateUser_thenBadRequestPlusMessage() {
            // Given
            CreateUserDto createUserDto = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");

            // When
            UserDto userDto =
                    RestAssured
                            .given()
                            .body(createUserDto)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.CREATED.value())
                            .extract()
                            .as(UserDto.class);

            String message =
                    RestAssured
                            .given()
                            .body(createUserDto)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The email address is already in use.");
        }

        @Test
        void givenInvalidFirstnameInCreateUserDto_whenCreateUser_thenBadRequestMessage() {
            // Given
            CreateUserDto createUserDto = getUser("", "Voeten", "jordi@mail.com", "Belgium", "number");

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createUserDto)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The firstname of the user is required.");
        }

        @Test
        void givenInvalidLastnameInCreateUserDto_whenCreateUser_thenBadRequestMessage() {
            // Given
            CreateUserDto createUserDto = getUser("Jordi", "", "jordi@mail.com", "Belgium", "number");

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createUserDto)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The lastname of the user is required.");
        }

        @Test
        void givenInvalidEmailInCreateUserDto_whenCreateUser_thenBadRequestMessage() {
            // Given
            CreateUserDto createUserDto = getUser("Jordi", "Voeten", "", "Belgium", "number");

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createUserDto)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The email address of the user is required.");
        }

        @Test
        void givenInvalidAddressInCreateUserDto_whenCreateUser_thenBadRequestMessage() {
            // Given
            CreateUserDto createUserDto = getUser("Jordi", "Voeten", "jordid@mail.com", "", "number");

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createUserDto)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The address of the user is required.");
        }

        @Test
        void givenInvalidPhoneNumberInCreateUserDto_whenCreateUser_thenBadRequestMessage() {
            // Given
            CreateUserDto createUserDto = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "");

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createUserDto)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The phone number of the user is required.");
        }

    }

    @Nested
    @DisplayName("Getter tests of the userController")
    class GetAllUsersTest {
        @Test
        void givenAThreeUsersThatAreAdded_whenGettingAllUsers_thenGetListOfUserDto() {
            // Given
            CreateUserDto createUserDto1 = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");
            CreateUserDto createUserDto2 = getUser("Jordi", "Voeten", "jordi2@mail.com", "Belgium", "number");
            CreateUserDto createUserDto3 = getUser("Jordi", "Voeten", "jordi3@mail.com", "Belgium", "number");
            UserDto userDto1 = getUserDtoAfterAdding(createUserDto1);
            UserDto userDto2 = getUserDtoAfterAdding(createUserDto2);
            UserDto userDto3 = getUserDtoAfterAdding(createUserDto3);
            List<UserDto> validUserDtoList = List.of(userDto1, userDto2, userDto3);
            // When
            UserDto[] foundList =
                    RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .header("authorization", admin)
                            .when()
                            .port(port)
                            .get("/users")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.OK.value())
                            .extract()
                            .as(UserDto[].class);

            Assertions.assertThat(Arrays.stream(foundList).map(UserDto::getId).toList())
                    .containsAll(validUserDtoList.stream().map(UserDto::getId).toList());
            Assertions.assertThat(Arrays.stream(foundList).map(UserDto::getEmail).toList())
                    .containsAll(validUserDtoList.stream().map(UserDto::getEmail).toList());

        }

        @Test
        void givenAThreeUsersThatAreAdded_whenGettingTheFirstUser_thenGetTheCorrectUser() {
            // Given
            CreateUserDto createUserDto1 = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");
            CreateUserDto createUserDto2 = getUser("Jordi", "Voeten", "jordi2@mail.com", "Belgium", "number");
            CreateUserDto createUserDto3 = getUser("Jordi", "Voeten", "jordi3@mail.com", "Belgium", "number");
            UserDto userDto1 = getUserDtoAfterAdding(createUserDto1);
            UserDto userDto2 = getUserDtoAfterAdding(createUserDto2);
            UserDto userDto3 = getUserDtoAfterAdding(createUserDto3);
            // When
            UserDto found =
                    RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .header("authorization", admin)
                            .when()
                            .port(port)
                            .get("/users/" + userDto1.getId())
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.OK.value())
                            .extract()
                            .as(UserDto.class);

            Assertions.assertThat(found.getId()).isEqualTo(userDto1.getId());
        }

        @Test
        void givenAThreeUsers_whenGettingAUserByInvalidId_thenGetTheCorrectErrormessage() {
            // Given
            CreateUserDto createUserDto1 = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");
            CreateUserDto createUserDto2 = getUser("Jordi", "Voeten", "jordi2@mail.com", "Belgium", "number");
            CreateUserDto createUserDto3 = getUser("Jordi", "Voeten", "jordi3@mail.com", "Belgium", "number");
            UserDto userDto1 = getUserDtoAfterAdding(createUserDto1);
            UserDto userDto2 = getUserDtoAfterAdding(createUserDto2);
            // When
            String message =
                    RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .header("authorization", admin)
                            .when()
                            .port(port)
                            .get("/users/invalid")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("User with id: invalid does not exist");
        }

        @Test
        void givenAThreeUsers_whenGettingAUserWithInvalidAutorisation_thenGetTheCorrectErrormessage() {
            // Given
            CreateUserDto createUserDto1 = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");
            CreateUserDto createUserDto2 = getUser("Jordi", "Voeten", "jordi2@mail.com", "Belgium", "number");
            CreateUserDto createUserDto3 = getUser("Jordi", "Voeten", "jordi3@mail.com", "Belgium", "number");
            UserDto userDto1 = getUserDtoAfterAdding(createUserDto1);
            UserDto userDto2 = getUserDtoAfterAdding(createUserDto2);

            String user = "Basic " + Base64.getEncoder().encodeToString((userDto1.getEmail() + ":").getBytes(StandardCharsets.UTF_8));

            // When
            String message =
                    RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .header("authorization", user)
                            .when()
                            .port(port)
                            .get("/users/invalid")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("This user is not authorized to preform this action.");
        }

        @Test
        void givenAThreeUsers_whenGettingAUserWithNullAutorisation_thenGetTheCorrectErrormessage() {
            // Given
            CreateUserDto createUserDto1 = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");
            CreateUserDto createUserDto2 = getUser("Jordi", "Voeten", "jordi2@mail.com", "Belgium", "number");
            CreateUserDto createUserDto3 = getUser("Jordi", "Voeten", "jordi3@mail.com", "Belgium", "number");
            UserDto userDto1 = getUserDtoAfterAdding(createUserDto1);
            UserDto userDto2 = getUserDtoAfterAdding(createUserDto2);

            String user = "";

            // When
            String message =
                    RestAssured
                            .given()
                            .contentType(ContentType.JSON)
                            .header("authorization", user)
                            .when()
                            .port(port)
                            .get("/users/invalid")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The username or password is incorrect.");
        }

        private UserDto getUserDtoAfterAdding(CreateUserDto createUserDto1) {
            return RestAssured
                    .given()
                    .body(createUserDto1)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .port(port)
                    .post("/users")
                    .then()
                    .extract()
                    .as(UserDto.class);
        }
    }


    private CreateUserDto getUser(String firstname, String lastname, String email, String address, String phoneNumber) {
        return CreateUserDto.builder()
                .firstName(firstname)
                .lastName(lastname)
                .email(email)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}