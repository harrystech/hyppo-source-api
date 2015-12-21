package com.harrys.hyppo.client.v1.model;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

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


    public UUID getJobId() {
        return jobId;
    }

}
