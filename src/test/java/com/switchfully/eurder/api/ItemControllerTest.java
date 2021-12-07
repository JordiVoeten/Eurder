package com.switchfully.eurder.api;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.domain.item.StockLevel;
import com.switchfully.eurder.domain.item.dto.CreateItemDto;
import com.switchfully.eurder.domain.item.dto.ItemDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ItemControllerTest {
    @Value("${server.port}")
    private int port;

    @Test
    void givenValidCreateItemDto_whenCreateItem_thenItemDtoGetsReturnedCorrectly() {
        // Given
        String admin = "Basic " + Base64.getEncoder().encodeToString("admin@hotmail.com:".getBytes(StandardCharsets.UTF_8));
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
                        .header("authorization",admin)
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
}