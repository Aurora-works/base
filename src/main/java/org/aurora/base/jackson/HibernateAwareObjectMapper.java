package org.aurora.base.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class HibernateAwareObjectMapper extends ObjectMapper {

    public HibernateAwareObjectMapper() {

        JSONUtils.registerModule(this);

        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        this.registerModule(emptyStringToNullModule());
    }

    private static SimpleModule emptyStringToNullModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(String.class, new StdDeserializer<>(String.class) {
            @Override
            public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                String result = StringDeserializer.instance.deserialize(jsonParser, deserializationContext);
                if (StringUtils.isEmpty(result)) {
                    return null;
                }
                return result;
            }
        });
        return module;
    }
}
