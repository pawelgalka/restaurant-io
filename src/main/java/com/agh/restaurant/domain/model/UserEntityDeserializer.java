package com.agh.restaurant.domain.model;

import com.agh.restaurant.domain.model.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class UserEntityDeserializer extends KeyDeserializer {

    @Override
    public UserEntity deserializeKey(
            String key,
            DeserializationContext ctxt) throws IOException,
            JsonProcessingException {

        return null;
    }
}