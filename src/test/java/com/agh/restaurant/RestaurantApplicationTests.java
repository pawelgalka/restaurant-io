package com.agh.restaurant;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RestaurantApplicationTests {

    @Test
    void contextLoads() {
    }

    @AfterAll
    static void tearDown(){

    }

}
