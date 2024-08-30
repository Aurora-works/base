package org.aurora.base.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.log4j.Log4j2;
import org.aurora.base.common.CommonConstant;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
public class JSONUtils {

    public static String writeValueAsString(Object value) {
        ObjectMapper objectMapper = new ObjectMapper();
        registerModule(objectMapper);
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static <T> T readValue(String value, TypeReference<T> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        registerModule(objectMapper);
        try {
            return objectMapper.readValue(value, typeReference);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static String checkValue(String value) {
        JsonNode node = readTree(value);
        if (node != null) {
            Set<String> valueNodes = new HashSet<>();
            Set.of(CommonConstant.NO_LOG_REQUEST_PARAMS).forEach(param -> {
                List<JsonNode> nodes = node.findValues(param);
                nodes.forEach(n -> {
                    if (n.isArray()) {
                        ((ArrayNode) n).removeAll();
                    } else if (n.isObject()) {
                        ((ObjectNode) n).removeAll();
                    } else {
                        valueNodes.add(param);
                    }
                });
            });
            valueNodes.forEach(v -> {
                List<JsonNode> parents = node.findParents(v);
                parents.forEach(p -> {
                    ((ObjectNode) p).put(v, (String) null);
                });
            });
            return writeValueAsString(node);
        }
        return null;
    }

    public static JsonNode readTree(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        registerModule(objectMapper);
        try {
            return objectMapper.readTree(content);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static void registerModule(ObjectMapper objectMapper) {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        javaTimeModule.addSerializer(new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
        javaTimeModule.addSerializer(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        objectMapper.registerModule(javaTimeModule);
        objectMapper.registerModule(new Hibernate6Module());
    }
}
