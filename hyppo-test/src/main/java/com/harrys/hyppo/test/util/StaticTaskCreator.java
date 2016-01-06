package com.harrys.hyppo.test.util;

import com.harrys.hyppo.source.api.task.CreateIngestionTasks;
import com.harrys.hyppo.source.api.task.IngestionTaskCreator;
import com.typesafe.config.Config;

import java.util.Collections;
import java.util.List;

/**
 * Created by jpetty on 1/5/16.
 */
public final class StaticTaskCreator implements IngestionTaskCreator {

    private List<Config> taskArguments;

    public StaticTaskCreator(final Config taskArguments) {
        this(Collections.singletonList(taskArguments));
    }

    public StaticTaskCreator(final List<Config> taskArguments){
        this.taskArguments = taskArguments;
    }

    @Override
    public void createIngestionTasks(final CreateIngestionTasks operation) throws Exception {
        taskArguments.forEach(operation::createTaskWithArgs);
    }
}
