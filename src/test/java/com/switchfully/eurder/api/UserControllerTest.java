package com.switchfully.eurder.api;

import com.switchfully.eurder.domain.user.dto.CreateUserDto;
import com.switchfully.eurder.domain.user.dto.UserDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {
    @Value("${server.port}")
    private int port;

    @Test
    void givenValidCreateUserDto_whenCreateUser_thenUserDtoGetsReturnedCorrectly() {
        // Given
        CreateUserDto createUserDto = new CreateUserDto()
                .setFirstName("Jordi")
                .setLastName("Voeten")
                .setEmail("jordi@mail.com")
                .setAddress("Belgium")
                .setPhoneNumber("number");

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
        CreateUserDto createUserDto = new CreateUserDto()
                .setFirstName("Jordi")
                .setLastName("Voeten")
                .setEmail("jordi@mail.com")
                .setAddress("Belgium")
                .setPhoneNumber("number");

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
        CreateUserDto createUserDto = new CreateUserDto()
                .setFirstName("")
                .setLastName("Voeten")
                .setEmail("jordi@mail.com")
                .setAddress("Belgium")
                .setPhoneNumber("number");

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
        CreateUserDto createUserDto = new CreateUserDto()
                .setFirstName("Jordi")
                .setLastName("")
                .setEmail("jordi@mail.com")
                .setAddress("Belgium")
                .setPhoneNumber("number");

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
        CreateUserDto createUserDto = new CreateUserDto()
                .setFirstName("Jordi")
                .setLastName("Voeten")
                .setEmail("")
                .setAddress("Belgium")
                .setPhoneNumber("number");

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
        CreateUserDto createUserDto = new CreateUserDto()
                .setFirstName("Jordi")
                .setLastName("Voeten")
                .setEmail("jordid@mail.com")
                .setAddress("")
                .setPhoneNumber("number");

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
        CreateUserDto createUserDto = new CreateUserDto()
                .setFirstName("Jordi")
                .setLastName("Voeten")
                .setEmail("jordi@mail.com")
                .setAddress("Belgium")
                .setPhoneNumber("");

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