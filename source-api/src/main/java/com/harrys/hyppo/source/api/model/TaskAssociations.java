package com.harrys.hyppo.source.api.model;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jpetty on 11/9/15.
 */
public final class TaskAssociations {

    public static final List<DataIngestionTask> resetJobReferences(final DataIngestionJob job, final List<DataIngestionTask> tasks) {
        if (job == null){
            return stripJobReferences(tasks);
        } else {
            return tasks.stream().map(t -> job.equals(t.getIngestionJob()) ? t : t.cloneWithJob(job)).collect(Collectors.toList());
        }
    }

    public static final List<DataIngestionTask> stripJobReferences(final List<DataIngestionTask> tasks) {
        return tasks.stream().map(t -> (t.getIngestionJob() == null) ? t : t.cloneWithJob(null)).collect(Collectors.toList());
    }
}
