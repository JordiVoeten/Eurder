package com.switchfully.eurder.api;

import com.switchfully.eurder.domain.Order.Order;
import com.switchfully.eurder.domain.Order.dto.CreateOrderDto;
import com.switchfully.eurder.domain.Order.dto.ItemGroupDto;
import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Item;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.domain.item.dto.CreateItemDto;
import com.switchfully.eurder.domain.item.dto.ItemDto;
import com.switchfully.eurder.domain.user.dto.CreateUserDto;
import com.switchfully.eurder.domain.user.dto.UserDto;
import com.switchfully.eurder.service.ItemService;
import com.switchfully.eurder.service.OrderService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class OrderControllerTest {
    @Value("${server.port}")
    private int port;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItemService itemService;

    private String admin;

    @BeforeEach
    void setup() {
        admin = "Basic " + Base64.getEncoder().encodeToString("admin@hotmail.com:".getBytes(StandardCharsets.UTF_8));
    }

    @Nested
    @DisplayName("Create order tests")
    class createOrderTest {

        @Test
        void givenAValidUserAValidItemGroupDto_WhenCreateOrder_thenOrderIsCreated() {
            ItemDto itemDto = createItem();
            ItemGroupDto itemGroupDto = new ItemGroupDto();
            itemGroupDto.setItemId(itemDto.getId());
            itemGroupDto.setAmount(10);

            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.addItemToGroup(itemGroupDto);

            Price price =
                    RestAssured
                            .given()
                            .body(createOrderDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/orders")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.CREATED.value())
                            .extract()
                            .as(Price.class);
            BigDecimal valueToCheck = new BigDecimal(4990).setScale(2, RoundingMode.HALF_EVEN);
            Assertions.assertThat(price.getValue()).isEqualTo(valueToCheck);
            Assertions.assertThat(price.getCurrency()).isEqualTo(Currency.EUR);
        }

        @Test
        void givenAValidUserAInvalidItemGroupDto_WhenCreateOrder_thenOrderIsCreated() {
            ItemDto itemDto = createItem();
            ItemGroupDto itemGroupDto = new ItemGroupDto();
            itemGroupDto.setItemId(itemDto.getId());
            itemGroupDto.setAmount(-10);

            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.addItemToGroup(itemGroupDto);

            String message =
                    RestAssured
                            .given()
                            .body(createOrderDto)
                            .header("authorization", admin)
                            .accept(ContentType.JSON)
                            .contentType(ContentType.JSON)
                            .when()
                            .port(port)
                            .post("/orders")
                            .then()
                            .assertThat()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .extract()
                            .path("message");
            Assertions.assertThat(message).isEqualTo("The order can not have negative item amounts.");
        }
    }


    @Nested
    @DisplayName("Reorder order order tests")
    class ReOrderTest {
        @Test
        void givenAValidOrder_WhenReordering_thenOrderIsCreated() {
            ItemDto itemDto = createItem();
            ItemGroupDto itemGroupDto = new ItemGroupDto();
            itemGroupDto.setItemId(itemDto.getId());
            itemGroupDto.setAmount(4);

            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.addItemToGroup(itemGroupDto);
            createOrder(createOrderDto);
            Order order = orderService.getOrders().get(0);

            Price price = RestAssured
                    .given()
                    .body(createOrderDto)
                    .header("authorization", admin)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .port(port)
                    .post("/orders/{orderId}", order.getId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(Price.class);
            Assertions.assertThat(order.getTotalPrice().getValue()).isEqualTo(price.getValue());
            Assertions.assertThat(order.getTotalPrice().getCurrency()).isEqualTo(price.getCurrency());
        }

        @Test
        void givenAValidOrder_WhenReorderingAfterPriceChange_thenOrderIsCreated() {
            ItemDto itemDto = createItem();
            ItemGroupDto itemGroupDto = new ItemGroupDto();
            itemGroupDto.setItemId(itemDto.getId());
            itemGroupDto.setAmount(4);

            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.addItemToGroup(itemGroupDto);
            createOrder(createOrderDto);
            Order order = orderService.getOrders().get(0);

            Item item = itemService.getItemBy(itemDto.getId());
            item.setPrice(new Price(20, Currency.EUR));
            itemService.updateItem(item);

            Price price = RestAssured
                    .given()
                    .body(createOrderDto)
                    .header("authorization", admin)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .port(port)
                    .post("/orders/{orderId}", order.getId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .as(Price.class);
            Assertions.assertThat(order.getTotalPrice().getValue()).isNotEqualTo(price.getValue());
            Assertions.assertThat(new Price(80,Currency.EUR).getValue()).isEqualTo(price.getValue());
            Assertions.assertThat(new Price(80,Currency.EUR).getCurrency()).isEqualTo(price.getCurrency());
        }
        @Test
        void givenAValidOrder_WhenReorderingAsWrongUser_thenOrderIsCreated() {
            CreateUserDto createUserDto1 = getUser("Jordi", "Voeten", "jordi@mail.com", "Belgium", "number");
            UserDto userDto1 = getUserDtoAfterAdding(createUserDto1);
            String user = "Basic " + Base64.getEncoder().encodeToString((userDto1.getEmail() + ":").getBytes(StandardCharsets.UTF_8));

            ItemDto itemDto = createItem();
            ItemGroupDto itemGroupDto = new ItemGroupDto();
            itemGroupDto.setItemId(itemDto.getId());
            itemGroupDto.setAmount(4);

            CreateOrderDto createOrderDto = new CreateOrderDto();
            createOrderDto.addItemToGroup(itemGroupDto);
            createOrder(createOrderDto);
            Order order = orderService.getOrders().get(0);

            String message = RestAssured
                    .given()
                    .body(createOrderDto)
                    .header("authorization", user)
                    .accept(ContentType.JSON)
                    .contentType(ContentType.JSON)
                    .when()
                    .port(port)
                    .post("/orders/{orderId}", order.getId())
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .extract()
                    .path("message");
            Assertions.assertThat(message).isEqualTo("The order does not exist or the user is not the same.");
         }
    }

    public ItemDto createItem() {
        Price price = new Price(499, Currency.EUR);
        CreateItemDto createItemDto = new CreateItemDto()
                .setName("Phone")
                .setDescription("To call someone")
                .setAmount(20)
                .setPrice(price);

        // When
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
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDto.class);
    }

    private Price createOrder(CreateOrderDto createOrderDto) {
        return RestAssured
                .given()
                .body(createOrderDto)
                .header("authorization", admin)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .post("/orders")
                .then()
                .extract()
                .as(Price.class);
    }

    private CreateUserDto getUser(String firstname, String lastname, String email, String address, String phoneNumber) {
        return new CreateUserDto()
                .setFirstName(firstname)
                .setLastName(lastname)
                .setEmail(email)
                .setAddress(address)
                .setPhoneNumber(phoneNumber);
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