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
    public void serialize(SysUser user, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (!Hibernate.isInitialized(user)) {
            gen.writeNull();
            return;
        }
        gen.writeStartObject();
        gen.writeNumberField("id", user.getId());
        gen.writeStringField("username", user.getUsername());
        gen.writeStringField("nickname", user.getNickname());
        gen.writeStringField("isDeleted", user.getIsDeleted());
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(SysUser user, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        if (!Hibernate.isInitialized(user)) {
            gen.writeNull();
            return;
        }
        WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen, typeSer.typeId(user, JsonToken.START_OBJECT));
        gen.writeNumberField("id", user.getId());
        gen.writeStringField("username", user.getUsername());
        gen.writeStringField("nickname", user.getNickname());
        gen.writeStringField("isDeleted", user.getIsDeleted());
        typeSer.writeTypeSuffix(gen, typeIdDef);
    }
}
