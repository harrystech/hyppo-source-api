package com.harrys.hyppo.client.v1.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.*;

import java.io.IOException;

/**
 * Created by jpetty on 12/29/15.
 */
public final class Jackson2ConfigToJson {

    public static final class Jackson2Serializer extends JsonSerializer<Config> {
        private static final ConfigRenderOptions renderOptions   = ConfigRenderOptions.concise().setJson(true);
        private static final ConfigResolveOptions resolveOptions = ConfigResolveOptions.noSystem().setAllowUnresolved(false);

        @Override
        public final void serialize(Config value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
            if (!value.isResolved()){
                value = value.resolve(resolveOptions);
            }
            final String jsonObject = value.root().render(renderOptions);
            jgen.writeRawValue(jsonObject);
        }
    }

    public static final class Jackson2Deserializer extends JsonDeserializer<Config> {
        private static final ConfigParseOptions parseOptions = ConfigParseOptions.defaults().setSyntax(ConfigSyntax.JSON);

        @Override
        public final Config deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException {
            final ObjectNode node = jp.getCodec().readTree(jp);
            return ConfigFactory.parseString(node.toString(), parseOptions);
        }
    }
}
