package org.aurora.base.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.aurora.base.entity.sys.SysUser;
import org.hibernate.Hibernate;

import java.io.IOException;

public class JsonSysUserSerializer extends JsonSerializer<SysUser> {

    @Override
    public void serialize(SysUser user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (!Hibernate.isInitialized(user)) {
            jsonGenerator.writeNull();
            return;
        }
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", user.getId());
        jsonGenerator.writeStringField("username", user.getUsername());
        jsonGenerator.writeStringField("nickname", user.getNickname());
        jsonGenerator.writeStringField("isDeleted", user.getIsDeleted());
        jsonGenerator.writeEndObject();
    }

    @Override
    public void serializeWithType(SysUser value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        if (!Hibernate.isInitialized(value)) {
            gen.writeNull();
            return;
        }
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.START_OBJECT));
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("username", value.getUsername());
        gen.writeStringField("nickname", value.getNickname());
        gen.writeStringField("isDeleted", value.getIsDeleted());
        typeSer.writeTypeSuffix(gen, typeIdDef);
    }
}
