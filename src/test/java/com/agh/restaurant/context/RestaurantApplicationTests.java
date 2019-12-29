package com.agh.restaurant.context;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RestaurantApplicationTests {

    @Test
    void contextLoads() {
        assertThat(1).isEqualTo(1);
    }

    @AfterAll
    static void tearDown(){

    }

}
