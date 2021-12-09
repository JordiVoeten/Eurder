package com.switchfully.eurder.api;

import com.switchfully.eurder.domain.item.Currency;
import com.switchfully.eurder.domain.item.Price;
import com.switchfully.eurder.domain.item.dto.CreateItemDto;
import com.switchfully.eurder.domain.item.dto.ItemDto;
import com.switchfully.eurder.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemControllerEndToEndTest {
    @LocalServerPort
    private int port;
    String URI;
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setup() {
        URI = "http://localhost:" + port + "/items";
    }

    @Test
    void givenAnItemDtoWeWantToCreate_whenCreatingThatDto_thenCheckThatItemIsInRepository() {
        // Given
        CreateItemDto createItemDto = CreateItemDto.builder()
                .name("PhoneForEndToEnd")
                .description("For calling")
                .amount(10)
                .price(new Price(499.99, Currency.EUR)).build();

        // When
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth("admin@hotmail.com", "");
        HttpEntity<CreateItemDto> requestEntity = new HttpEntity<>(createItemDto, headers);

        ResponseEntity<ItemDto> entity = restTemplate.postForEntity(URI, requestEntity, ItemDto.class);

        // Then
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

}