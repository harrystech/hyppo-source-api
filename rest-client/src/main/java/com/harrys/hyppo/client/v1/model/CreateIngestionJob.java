package com.harrys.hyppo.client.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


/**
 * Created by jpetty on 12/18/15.
 */
public final class CreateIngestionJob {

    @JsonProperty("sourceName")
    private final String sourceName;

    @JsonProperty("parameters")
    @JsonDeserialize(using = Jackson2ConfigToJson.Jackson2Deserializer.class)
    @JsonSerialize(using = Jackson2ConfigToJson.Jackson2Serializer.class)
    private final Config parameters;

    @JsonCreator
    public CreateIngestionJob(
            @JsonProperty("sourceName") final String sourceName,
            @JsonProperty("parameters") final Config parameters
    ) {
        this.sourceName = sourceName;
        this.parameters = parameters;
    }

    private CreateIngestionJob(Builder builder){
        this.sourceName = builder.sourceName;
        this.parameters = builder.parameters;
    }

    public String getSourceName() {
        return sourceName;
    }

    public Config getParameters() {
        if (parameters == null){
            return ConfigFactory.empty();
        } else {
            return parameters;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    public static final class Builder {
        private String sourceName;
        private Config parameters;

        private Builder() {
        }

        public Builder withSourceName(String val) {
            sourceName = val;
            return this;
        }

        public Builder withParameters(Config val) {
            parameters = val;
            return this;
        }

        public CreateIngestionJob build() {
            return new CreateIngestionJob(this);
        }
    }
}
