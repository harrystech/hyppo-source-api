package com.harrys.hyppo.client.v1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by jpetty on 12/18/15.
 */
public final class IngestionJobCreated {

    @JsonProperty("jobId")
    private final UUID jobId;

    @JsonCreator
    public IngestionJobCreated(
            @JsonProperty("jobId") final UUID jobId
    ) {
        this.jobId = jobId;
    }


    public final UUID getJobId() {
        return jobId;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private UUID jobId;

        private Builder() {
        }

        public final Builder withJobId(final UUID val) {
            jobId = val;
            return this;
        }

        public final IngestionJobCreated build() {
            return new IngestionJobCreated(jobId);
        }
    }
}
