package com.harrys.hyppo.source.api.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jpetty on 11/9/15.
 */
public final class TaskAssociations {

    public static final List<DataIngestionTask> resetJobReferences(final DataIngestionJob job, final List<DataIngestionTask> tasks) {
        return tasks.stream().
                map(t -> (job.equals(t.getIngestionJob())) ? t : new DataIngestionTask(job, t.getTaskNumber(), t.getTaskArguments())).
                collect(Collectors.toList());
    }

    public static final List<DataIngestionTask> stripJobReferences(final List<DataIngestionTask> tasks) {
        return tasks.stream().
                map(t -> (t.getIngestionJob() == null) ? t : new DataIngestionTask(null, t.getTaskNumber(), t.getTaskArguments())).
                collect(Collectors.toList());
    }
}
