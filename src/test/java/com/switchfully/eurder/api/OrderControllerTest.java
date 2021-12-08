package com.switchfully.eurder.api;

import com.switchfully.eurder.domain.Order.Order;
import com.switchfully.eurder.domain.Order.dto.CreateOrderDto;
import com.switchfully.eurder.domain.Order.dto.ItemGroupDto;
import com.switchfully.eurder.domain.Order.dto.ItemGroupReportDto;
import com.switchfully.eurder.domain.Order.dto.OrderDto;
import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.domain.item.dto.CreateItemDto;
import com.switchfully.eurder.domain.item.dto.ItemDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private String admin;

    @BeforeEach
    void setup() {
        admin = "Basic " + Base64.getEncoder().encodeToString("admin@hotmail.com:".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void givenAValidUserAValidItemGroupDto_WhenCreateOrder_thenOrderIsCreated() {
        String itemId = createItem();
        ItemGroupDto itemGroupDto = new ItemGroupDto();
        itemGroupDto.setItemId(itemId);
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


    public String createItem() {
        Price price = new Price(499, Currency.EUR);
        CreateItemDto createItemDto = new CreateItemDto()
                .setName("Phone")
                .setDescription("To call someone")
                .setAmount(20)
                .setPrice(price);

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
        return itemDto.getId();
    }
}