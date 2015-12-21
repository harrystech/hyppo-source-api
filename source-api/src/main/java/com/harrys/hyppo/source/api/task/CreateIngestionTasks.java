package com.harrys.hyppo.source.api.task;

import com.harrys.hyppo.source.api.model.DataIngestionJob;
import com.harrys.hyppo.source.api.model.IngestionSource;
import com.harrys.hyppo.source.api.model.TaskBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

import java.util.Map;

/**
 * Created by jpetty on 7/17/15.
 */
public final class CreateIngestionTasks {

    private static final Config sharedEmptyConfig = ConfigFactory.empty();

    private final DataIngestionJob job;

    private final TaskBuilder builder;

    public CreateIngestionTasks(final DataIngestionJob job){
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

    public final CreateIngestionTasks createTaskWithArgs(final Config arguments){
        this.builder.addTask(arguments);
        return this;
    }

    public final CreateIngestionTasks createTaskWithArgs(final Map<String, Object> arguments){
        final Config value;
        try {
            value = ConfigValueFactory.fromMap(arguments).toConfig();
        } catch (ConfigException ce) {
            throw new IllegalArgumentException("Can't create a Config object from arguments!", ce);
        }
        return this.createTaskWithArgs(value);
    }

    public final CreateIngestionTasks createTaskWithoutArgs(){
        return this.createTaskWithArgs(sharedEmptyConfig);
    }
}
