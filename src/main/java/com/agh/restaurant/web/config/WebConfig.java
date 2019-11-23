package com.agh.restaurant.web.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    public final static String MODEL_MAPPER = "ModelMapperWeb";

    @Bean(name = MODEL_MAPPER)
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();


        return mapper;
    }
}
