package com.switchfully.eurder;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class EurderApplicationTest {
    @Test
    void noUseJustWant100coverage() {
        Assertions.assertThatThrownBy(() -> EurderApplication.main(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}