package com.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JacksonCheck {

    public JacksonCheck(ObjectMapper objectMapper) {
        System.out.println(">>> Mapper modules: " + objectMapper.getRegisteredModuleIds());
    }
}
