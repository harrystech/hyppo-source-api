package com.harrys.hyppo.source.api.model;

import com.typesafe.config.Config;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by jpetty on 7/6/15.
 */
public final class DataIngestionTask implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ingestionJob")
    private final DataIngestionJob ingestionJob;

    @JsonProperty("taskNumber")
    private final int taskNumber;

    @JsonSerialize(using = ConfigToJson.Serializer.class)
    @JsonDeserialize(using = ConfigToJson.Deserializer.class)
    @JsonProperty("taskArguments")
    private final Config taskArguments;

    @JsonCreator
    public DataIngestionTask(
            @JsonProperty("ingestionJob")   final DataIngestionJob ingestionJob,
            @JsonProperty("taskNumber")     final int taskNumber,
            @JsonProperty("taskArguments")  final Config taskArguments
    ){
        this.ingestionJob = ingestionJob;
        this.taskNumber = taskNumber;
        this.taskArguments = taskArguments;
    }


    public final DataIngestionJob getIngestionJob() {
        return ingestionJob;
    }

    @JsonIgnore
    public final UUID getIngestionJobId(){
        return this.getIngestionJob().getId();
    }

    public final int getTaskNumber() {
        return taskNumber;
    }

    public final Config getTaskArguments() {
        return taskArguments;
    }

    @JsonIgnore
    public final DataIngestionTask cloneWithJob(final DataIngestionJob job) {
        return new DataIngestionTask(job, taskNumber, taskArguments);
    }

    @Override
    public final boolean equals(Object check){
        if (check instanceof DataIngestionTask){
            final DataIngestionTask other = (DataIngestionTask)check;
            return (ingestionJob.equals(other.getIngestionJob())
                    && taskNumber == other.getTaskNumber()
                    && taskArguments.equals(other.getTaskArguments())
            );
        } else {
            return super.equals(check);
        }
    }

}
