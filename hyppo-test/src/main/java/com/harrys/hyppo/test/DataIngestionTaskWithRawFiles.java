package com.harrys.hyppo.test;

import com.harrys.hyppo.source.api.model.DataIngestionTask;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by pettyjamesm on 12/28/15.
 */
public final class DataIngestionTaskWithRawFiles {

    private final DataIngestionTask task;

    private final List<File> rawDataFiles;

    public DataIngestionTaskWithRawFiles(final DataIngestionTask task, final List<File> rawDataFiles){
        this.task           = task;
        this.rawDataFiles   = Collections.unmodifiableList(rawDataFiles);
    }

    public final DataIngestionTask getTask() {
        return task;
    }

    public final List<File> getRawDataFiles() {
        return rawDataFiles;
    }
}
