package com.mm4096.midicontroller.parser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mm4096.midicontroller.classes.ConfigClass;

import java.io.IOException;

public class ConfigClassSerializer extends JsonSerializer<ConfigClass> {
    @Override
    public void serialize(ConfigClass value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("alias", value.getAlias());
        gen.writeStringField("bankId", value.getBankId());
        gen.writeEndObject();
    }
}