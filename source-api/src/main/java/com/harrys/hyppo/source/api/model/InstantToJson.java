package com.harrys.hyppo.source.api.model;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.util.ISO8601Utils;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by jpetty on 5/9/16.
 */
public final class InstantToJson {

    public static final class Serializer extends JsonSerializer<Instant> {
        private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

        public Serializer(){ }

        @Override
        public final void serialize(Instant value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            if (value == null){
                jgen.writeNull();
            } else {
                jgen.writeString(ISO8601Utils.format(Date.from(value), true, UTC));
            }
        }
    }

    public static final class Deserializer extends JsonDeserializer<Instant> {

        public Deserializer(){ }

        @Override
        public final Instant deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            final String value = jp.readValueAs(String.class);
            if (value == null){
                return null;
            } else {
                final Date date = ISO8601Utils.parse(value);
                return date.toInstant();
            }
        }
    }
}
