package com.harrys.hyppo.source.api.task;

/**
 * Created by jpetty on 7/17/15.
 */
public interface IngestionTaskCreator {
    void createIngestionTasks(CreateIngestionTasks operation) throws Exception;
}
