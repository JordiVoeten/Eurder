package com.switchfully.eurder.api;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.domain.item.StockLevel;
import com.switchfully.eurder.domain.item.dto.CreateItemDto;
import com.switchfully.eurder.domain.item.dto.ItemDto;
import com.switchfully.eurder.domain.item.dto.UpdateItemDto;
import com.switchfully.eurder.domain.user.dto.CreateUserDto;
import com.switchfully.eurder.domain.user.dto.UserDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.switchfully.eurder.domain.item.StockLevel.STOCK_MEDIUM;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {
    @Value("${server.port}")
    private int port;
    private String admin;

    @BeforeEach
    void setup() {
        admin = "Basic " + Base64.getEncoder().encodeToString("admin@hotmail.com:".getBytes(StandardCharsets.UTF_8));
    }

    @Nested
    @DisplayName("Creating an item tests")
    class CreateItemTests {
        @Test
        void givenValidCreateItemDto_whenCreateItem_thenItemDtoGetsReturnedCorrectly() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);

            // When
            ItemDto itemDto =
                    RestAssured
                            .given()
                            .body(createItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.CREATED.value())
                            .extract()
                            .as(ItemDto.class);

            Assertions.assertThat(itemDto.getName()).isEqualTo(createItemDto.getName());
            Assertions.assertThat(itemDto.getDescription()).isEqualTo(createItemDto.getDescription());
            Assertions.assertThat(itemDto.getAmount()).isEqualTo(createItemDto.getAmount());
            Assertions.assertThat(itemDto.getPrice().getValue()).isEqualTo(createItemDto.getPrice().getValue());
            Assertions.assertThat(itemDto.getPrice().getCurrency()).isEqualTo(createItemDto.getPrice().getCurrency());
            Assertions.assertThat(itemDto.getStockLevel()).isEqualTo(StockLevel.getStockLevelForAmount(createItemDto.getAmount()));
        }

        @Test
        void givenValidCreateItemDto_whenCreateItemThatItemTwice_thenCorrectErrorMessage() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);

            // When
            RestAssured
                    .given()
                    .body(createItemDto)
                    .header("authorization", admin)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .port(port)
                    .post("/items");

            String message =
                    RestAssured
                            .given()
                            .body(createItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The item with name: " + createItemDto.getName() + " already exists.");

        }

        @Test
        void givenInvalidCreateItemDtoNoName_whenCreateItem_thenGetCorrectErrorMessage() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);
            createItemDto.setName(null);

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The name of the item is required.");
        }

        @Test
        void givenInvalidCreateItemDtoNoDescription_whenCreateItem_thenGetCorrectErrorMessage() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);
            createItemDto.setDescription(null);

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The description of the item is required.");
        }

        @Test
        void givenInvalidCreateItemDtoNoPrice_whenCreateItem_thenGetCorrectErrorMessage() {
            // Given
            Price price = null;
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The price of the item is required.");
        }

        @Test
        void givenInvalidCreateItemDtoNoValidPriceValue_whenCreateItem_thenGetCorrectErrorMessage() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);
            createItemDto.setPrice(new Price(-1, Currency.EUR));

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The price of the item has to be larger than 0.");
        }

        @Test
        void givenInvalidCreateItemDtoNegativeAmount_whenCreateItem_thenGetCorrectErrorMessage() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);
            createItemDto.setAmount(-1);

            // When
            String message =
                    RestAssured
                            .given()
                            .body(createItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");

            Assertions.assertThat(message).isEqualTo("The amount of the item has to be larger than 0.");
        }
    }

    @Nested
    @DisplayName("Getter for item tests")
    class GetItemOverviewTests {
        @Test
        void givenTwoItems_whenGettingItemOverview_thenGetListInCorrectSequence() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto1 = getCreateItemDto("Phone", "To call others", 10, price);
            CreateItemDto createItemDto2 = getCreateItemDto("otherItem", "To call others", 2, price);
            ItemDto itemDto1 = CreateItem(createItemDto1);
            ItemDto itemDto2 = CreateItem(createItemDto2);
            List<ItemDto> valid = List.of(itemDto2, itemDto1);
            // When
            ItemDto[] itemDto =
                    RestAssured
                            .given()
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .get("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.OK.value())
                            .extract()
                            .as(ItemDto[].class);
            Assertions.assertThat(valid.stream().map(ItemDto::getId).toList())
                    .containsAll(Arrays.stream(itemDto).map(ItemDto::getId).toList());
        }

        @Test
        void givenTwoItems_whenGettingItemOverviewUsingACustomer_thenGetListInCorrectSequence() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto1 = getCreateItemDto("Phone", "To call others", 10, price);
            CreateItemDto createItemDto2 = getCreateItemDto("otherItem", "To call others", 2, price);

            CreateUserDto createUserDto = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");
            UserDto userDto1 = getUserDtoAfterAdding(createUserDto);
            String user = "Basic " + Base64.getEncoder().encodeToString((userDto1.getEmail() + ":").getBytes(StandardCharsets.UTF_8));


            ItemDto itemDto1 = CreateItem(createItemDto1);
            ItemDto itemDto2 = CreateItem(createItemDto2);
            List<ItemDto> valid = List.of(itemDto2, itemDto1);
            // When
            String message =
                    RestAssured
                            .given()
                            .header("authorization", user)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .get("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");
            Assertions.assertThat(message).isEqualTo("This user is not authorized to preform this action.");
        }

        @Test
        void givenTwoItems_whenGettingItemOverviewOfMediumStock_thenGetListInCorrectSequence() {
            // Given
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto1 = getCreateItemDto("Phone", "To call others", 10, price);
            CreateItemDto createItemDto2 = getCreateItemDto("otherItem", "To call others", 5, price);

            ItemDto itemDto1 = CreateItem(createItemDto1);
            ItemDto itemDto2 = CreateItem(createItemDto2);
            List<ItemDto> valid = List.of(itemDto2);
            // When
            ItemDto[] itemDto =
                    RestAssured
                            .given()
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .queryParam("stockLevel", STOCK_MEDIUM)
                            .when()
                            .port(port)
                            .get("/items")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.OK.value())
                            .extract()
                            .as(ItemDto[].class);

            Assertions.assertThat(valid.stream().map(ItemDto::getId).toList())
                    .isEqualTo(Arrays.stream(itemDto).map(ItemDto::getId).toList());
        }
    }

    @Nested
    @DisplayName("Update item tests")
    class UpdateItemTests {
        @Test
        void givenAnItem_whenUpdatingTheItem_thenGetCorrectItem() {
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);
            ItemDto itemDto = CreateItem(createItemDto);
            UpdateItemDto updateItemDto = UpdateItemDto.builder()
                    .name("anotherPhone")
                    .description("differentDescription")
                    .amount(1)
                    .price(new Price(200, Currency.EUR))
                    .build();

            ItemDto found =
                    RestAssured
                            .given()
                            .body(updateItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .put("/items/{itemId}", itemDto.getId())
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.CREATED.value())
                            .extract()
                            .as(ItemDto.class);
            Assertions.assertThat(found.getId()).isEqualTo(itemDto.getId());
            Assertions.assertThat(found.getName()).isEqualTo(updateItemDto.getName());
            Assertions.assertThat(found.getDescription()).isEqualTo(updateItemDto.getDescription());
            Assertions.assertThat(found.getAmount()).isEqualTo(updateItemDto.getAmount());
            Assertions.assertThat(found.getPrice().getValue()).isEqualTo(updateItemDto.getPrice().getValue());
            Assertions.assertThat(found.getPrice().getCurrency()).isEqualTo(updateItemDto.getPrice().getCurrency());
        }

        @Test
        void givenAnItem_whenUpdatingTheItemAsCustomer_thenGetCorrectErrorMessage() {
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);
            ItemDto itemDto = CreateItem(createItemDto);
            UpdateItemDto updateItemDto = UpdateItemDto.builder().name("anotherPhone").build();

            CreateUserDto createUserDto = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");
            UserDto userDto1 = getUserDtoAfterAdding(createUserDto);
            String user = "Basic " + Base64.getEncoder().encodeToString((userDto1.getEmail() + ":").getBytes(StandardCharsets.UTF_8));

            String message =
                    RestAssured
                            .given()
                            .body(updateItemDto)
                            .header("authorization", user)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .put("/items/{itemId}", itemDto.getId())
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");
            Assertions.assertThat(message).isEqualTo("This user is not authorized to preform this action.");
        }

        @Test
        void givenAnItem_whenUpdatingTheItemToAnAlreadyExistingName_thenGetCorrectErrorMessage() {
            Price price = new Price(499, Currency.EUR);
            CreateItemDto createItemDto = getCreateItemDto("Phone", "To call others", 10, price);
            ItemDto itemDto = CreateItem(createItemDto);
            CreateItemDto createItemDto2 = getCreateItemDto("anotherPhone", "To call others", 10, price);
            ItemDto itemDto2 = CreateItem(createItemDto2);
            UpdateItemDto updateItemDto = UpdateItemDto.builder().name("anotherPhone").build();

            String message =
                    RestAssured
                            .given()
                            .body(updateItemDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .put("/items/{itemId}", itemDto.getId())
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");
            Assertions.assertThat(message).isEqualTo("The item with name: " + updateItemDto.getName() + " already exists.");
        }
    }


    private ItemDto CreateItem(CreateItemDto createItemDto) {
        return RestAssured
                .given()
                .body(createItemDto)
                .header("authorization", admin)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .post("/items")
                .then()
                .extract()
                .as(ItemDto.class);
    }

    private CreateItemDto getCreateItemDto(String name, String description, int amount, Price price) {
        return CreateItemDto.builder()
                .name(name)
                .description(description)
                .amount(amount)
                .price(price).build();
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