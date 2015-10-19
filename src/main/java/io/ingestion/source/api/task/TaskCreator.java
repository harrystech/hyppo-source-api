package io.ingestion.source.api.task;

/**
 * Created by jpetty on 7/17/15.
 */
@FunctionalInterface
public interface TaskCreator {
    void createTasks(CreateTasksForJob operation) throws Exception;
}
