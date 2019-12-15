package com.agh.restaurant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class RestaurantApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RestaurantApplication.class);
        application.run(new String[]{"--debug"});
    }

}
