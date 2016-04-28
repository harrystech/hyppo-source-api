package com.harrys.hyppo.source.api.model;

import com.typesafe.config.Config;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Created by jpetty on 7/6/15.
 */
public final class DataIngestionJob implements Serializable {
    private static final long serialVersionUID = 1L;

    private final IngestionSource ingestionSource;

    private final UUID id;

    @JsonSerialize(using = ConfigToJson.Serializer.class)
    @JsonDeserialize(using = ConfigToJson.Deserializer.class)
    private final Config parameters;

    @JsonProperty("startedAt")
    private final Instant startedAt;

    @JsonCreator
    public DataIngestionJob(
            @JsonProperty("ingestionSource")    final IngestionSource ingestionSource,
            @JsonProperty("id")                 final UUID id,
            @JsonProperty("parameters")         final Config parameters,
            @JsonProperty("startedAt")          final Instant startedAt
    ) {
        this.ingestionSource = ingestionSource;
        this.id = id;
        this.parameters = parameters;
        this.startedAt  = startedAt;
    }

    public final IngestionSource getIngestionSource() {
        return ingestionSource;
    }

    public final UUID getId() {
        return id;
    }

    public final Config getParameters() {
        return parameters;
    }

    public final Instant getStartedAt(){
        return this.startedAt;
    }

    @Override
    public final int hashCode(){
        return ingestionSource.hashCode() + id.hashCode() + parameters.hashCode() + startedAt.hashCode();
    }

    @Override
    public final  boolean equals(Object check){
        if (check instanceof DataIngestionJob){
            final DataIngestionJob other = (DataIngestionJob)check;
            return (
                    ingestionSource.equals(other.getIngestionSource()) &&
                    id.equals(other.getId()) &&
                    parameters.equals(other.getParameters()) &&
                    startedAt.equals(other.getStartedAt())
            );
        } else {
            return super.equals(check);
        }
    }

}
