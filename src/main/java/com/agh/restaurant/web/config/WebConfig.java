package com.agh.restaurant.web.config;

import com.agh.restaurant.domain.model.TestEntity;
import com.agh.restaurant.web.dto.test.TestJson;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    public final static String MODEL_MAPPER = "ModelMapperWeb";

    @Bean(name = MODEL_MAPPER)
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(new Converter<TestEntity, TestJson>() {

            public TestJson convert(MappingContext<TestEntity, TestJson> context) {
                TestEntity entity = context.getSource();
                TestJson testJson = context.getDestination();
                testJson.setOutId(entity.getId());
                testJson.setName(entity.getName());

                return testJson;
            }
        });

        return mapper;
    }
}
