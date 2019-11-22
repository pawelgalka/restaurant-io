package com.agh.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RestaurantApplication.class);
        application.run(new String[] { "--debug" });
    }

}
