package com.agh.restaurant.web.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("it")
@PropertySource("classpath:./application-it.properties")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser(username = "TEST_CUSTOMER", roles = { "CUSTOMER" }, password = "12345678")
public class CustomerApiTest {
    @Test
    void createRes_ReturnsOk(){
    }

    @Test
    void createRes_ReturnsError(){
    }

    @Test
    void updateRes_ReturnsOk(){
    }

    @Test
    void updateRes_ReturnsError(){}

    @Test
    void deleteRes_ReturnsOk(){}

}
