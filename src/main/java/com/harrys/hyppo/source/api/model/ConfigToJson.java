package com.harrys.hyppo.source.api.model;

import com.typesafe.config.*;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.node.ObjectNode;

import java.io.IOException;

/**
 * Created by jpetty on 7/14/15.
 */
public final class ConfigToJson {

    public static final class Serializer extends JsonSerializer<Config> {
        private static final ConfigRenderOptions renderOptions = ConfigRenderOptions.concise().setJson(true);

        public Serializer(){ }

        @Override
        public final void serialize(Config value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
            final String jsonObject = value.root().render(renderOptions);
            jgen.writeRawValue(jsonObject);
        }
    }

    public static final class Deserializer extends JsonDeserializer<Config> {
        private static final ConfigParseOptions parseOptions = ConfigParseOptions.defaults().setSyntax(ConfigSyntax.JSON);

        public Deserializer(){ }

        @Override
        public final Config deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            final ObjectNode node = (ObjectNode)jp.getCodec().readTree(jp);
            return ConfigFactory.parseString(node.toString(), parseOptions);
        }
    }
}
