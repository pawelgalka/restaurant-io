package com.agh.restaurant.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;

@Converter(autoApply = true)
public class RaportConverter implements AttributeConverter<FeedbackReport, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(FeedbackReport meta) {
        try {
            objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(meta);
//            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    @Override
    public FeedbackReport convertToEntityAttribute(String dbData) {
        try {

            return objectMapper.readValue(dbData, FeedbackReport.class);
        } catch (IOException ex) {
            return null;
        }
    }
}
