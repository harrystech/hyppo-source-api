package com.harrys.hyppo.source.api.task;

import com.harrys.hyppo.source.api.model.TaskBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.IngestionSource;

import java.util.Map;

/**
 * Created by jpetty on 7/17/15.
 */
public final class CreateTasksForJob {

    private static final Config sharedEmptyConfig = ConfigFactory.empty();

    private final DataIngestionJob job;

    private final TaskBuilder builder;

    public CreateTasksForJob(final DataIngestionJob job){
        this.job      = job;
        this.builder  = new TaskBuilder();
    }

    public final IngestionSource getSource(){
        return this.getJob().getIngestionSource();
    }

    public final DataIngestionJob getJob(){
        return this.job;
    }

    public final TaskBuilder getTaskBuilder(){
        return this.builder;
    }

    public final CreateTasksForJob createTaskWithArgs(final Config arguments){
        this.builder.addTask(arguments);
        return this;
    }

    public final CreateTasksForJob createTaskWithArgs(final Map<String, Object> arguments){
        final Config value = ConfigValueFactory.fromMap(arguments).toConfig();
        return this.createTaskWithArgs(value);
    }

    public final CreateTasksForJob createTaskWithoutArgs(){
        return this.createTaskWithArgs(sharedEmptyConfig);
    }
}
