package com.harrys.hyppo.source.api;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Created by jpetty on 11/9/15.
 */
public enum LogicalOperation {
    ValidateIntegration,
    CreateIngestionTasks,
    FetchProcessedData,
    FetchRawData,
    ProcessRawData,
    PersistProcessedData,
    HandleJobCompleted;


    @JsonValue
    public final String jsonName() {
        return this.name();
    }

    @JsonCreator
    public static final LogicalOperation fromString(final String value) {
        for (LogicalOperation o : LogicalOperation.values()){
            if (o.jsonName().equalsIgnoreCase(value)){
                return o;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown %s value: %s", LogicalOperation.class.getSimpleName(), value));
    }
}
