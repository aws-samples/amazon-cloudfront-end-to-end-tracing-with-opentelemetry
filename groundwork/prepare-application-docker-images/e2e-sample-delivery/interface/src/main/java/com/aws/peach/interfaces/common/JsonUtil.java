package com.aws.peach.interfaces.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    private final ObjectMapper mapper;

    public JsonUtil(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String serialize(Object obj) throws JsonException {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            String msg = String.format("failed to serialize object: %s", obj.toString());
            throw new JsonException(msg, e);
        }
    }
}
