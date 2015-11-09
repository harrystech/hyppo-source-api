package com.harrys.hyppo.source.api.task;

import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import com.harrys.hyppo.source.api.model.IngestionSource;
import com.harrys.hyppo.source.api.model.TaskAssociations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by jpetty on 11/9/15.
 */
public final class HandleJobCompleted {

    private final LocalDateTime completedAt;

    private final DataIngestionJob job;

    private final List<DataIngestionTask> tasks;

    public HandleJobCompleted(final LocalDateTime completedAt, final DataIngestionJob job, final List<DataIngestionTask> tasks){
        this.completedAt = completedAt;
        this.job    = job;
        this.tasks  = Collections.unmodifiableList(TaskAssociations.resetJobReferences(job, tasks));
    }

    public final LocalDateTime getCompletedAt(){
        return this.completedAt;
    }

    public final IngestionSource getSource(){
        return this.getJob().getIngestionSource();
    }

    public final DataIngestionJob getJob(){
        return this.job;
    }

    public final List<DataIngestionTask> getTasks(){
        return this.tasks;
    }

}
