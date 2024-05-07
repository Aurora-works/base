package org.aurora.base.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HibernateAwareObjectMapper extends ObjectMapper {

    public HibernateAwareObjectMapper() {
        JSONUtils.registerModule(this);
    }
}
