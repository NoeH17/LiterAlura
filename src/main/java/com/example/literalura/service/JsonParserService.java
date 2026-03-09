package com.example.literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class JsonParserService {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T parse(String json, Class<T> clazz) {

        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing JSON", e);
        }

    }
}