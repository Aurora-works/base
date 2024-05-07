package org.aurora.base.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GenericJsonRedisObjectMapper extends ObjectMapper {

    public GenericJsonRedisObjectMapper() {
        super.activateDefaultTyping(getPolymorphicTypeValidator(), DefaultTyping.NON_FINAL);
        JSONUtils.registerModule(this);
    }
}
