package com.harrys.hyppo.source.api.model;

import com.typesafe.config.Config;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * Created by jpetty on 7/7/15.
 */
public final class IngestionSource implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("configuration")
    @JsonSerialize(using = ConfigToJson.Serializer.class)
    @JsonDeserialize(using = ConfigToJson.Deserializer.class)
    private final Config configuration;

    @JsonCreator
    public IngestionSource(
            @JsonProperty("name") final String name,
            @JsonProperty("configuration") final Config configuration
    ){
        if (name == null){
            throw new IllegalArgumentException("name must not be null");
        }
        if (configuration == null){
            throw new IllegalArgumentException("configuration must not be null");
        }
        this.name = name;
        this.configuration = configuration;
    }

    public final String getName() {
        return name;
    }

    public final Config getConfiguration() {
        return configuration;
    }

    @Override
    public final boolean equals(final Object other){
        if (other instanceof IngestionSource){
            final IngestionSource otherSource = (IngestionSource)other;
            return (name.equals(otherSource.getName()) && configuration.equals(otherSource.getConfiguration()));
        } else {
            return super.equals(other);
        }
    }
}
