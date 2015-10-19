package com.harrys.hyppo.source.api.model;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigValueFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jpetty on 8/27/15.
 */
public final class TaskBuilder {

    private final List<DataIngestionTask> tasks;

    public TaskBuilder() {
        this.tasks = new ArrayList<>();
    }

    public final TaskBuilder addTask(Config taskArguments) {
        this.tasks.add(new DataIngestionTask(null, this.tasks.size() + 1, taskArguments));
        return this;
    }

    public final TaskBuilder addTask(final Map<String, Object> taskArguments) {
        final Config args = ConfigValueFactory.fromMap(taskArguments).toConfig();
        return this.addTask(args);
    }

    public final List<DataIngestionTask> build() {
        final ArrayList<DataIngestionTask> output = new ArrayList<DataIngestionTask>(this.tasks);
        return output;
    }
}
