package com.harrys.hyppo.client.v1.model;

import com.harrys.hyppo.source.api.model.ConfigToJson;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Created by jpetty on 12/18/15.
 */
public final class CreateIngestionJob {

    @JsonProperty("sourceName")
    private final String sourceName;

    @JsonProperty("parameters")
    @JsonDeserialize(using = ConfigToJson.Deserializer.class)
    @JsonSerialize(using = ConfigToJson.Serializer.class)
    private final Config parameters;

    @JsonCreator
    public CreateIngestionJob(
            @JsonProperty("sourceName") final String sourceName,
            @JsonProperty("parameters") final Config parameters
    ) {
        this.sourceName = sourceName;
        if (parameters == null){
            this.parameters = ConfigFactory.empty();
        } else {
            this.parameters = parameters;
        }
    }

    public String getSourceName() {
        return sourceName;
    }

    public Config getParameters() {
        return parameters;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String sourceName;
        private Config parameters;

        public Builder() {}

        public Builder withSourceName(String sourceName) {
            this.sourceName = sourceName;
            return this;
        }

        public Builder withParameters(Config parameters) {
            this.parameters = parameters;
            return this;
        }

        public CreateIngestionJob build() {
            return new CreateIngestionJob(sourceName, parameters);
        }
    }
}
