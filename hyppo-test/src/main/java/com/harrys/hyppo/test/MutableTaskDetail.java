package com.harrys.hyppo.test;

import com.harrys.hyppo.source.api.model.DataIngestionTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jpetty on 12/29/15.
 */
public final class MutableTaskDetail {

    private final DataIngestionTask task;

    private List<File> rawDataFiles;

    private File avroDataFile;

    public MutableTaskDetail(final DataIngestionTask task) {
        this.task = task;
        this.rawDataFiles = new ArrayList<>();
        this.avroDataFile = null;
    }

    public DataIngestionTask getTask() {
        return task;
    }

    public List<File> getRawDataFiles() {
        return rawDataFiles;
    }

    public File getAvroDataFile() {
        return avroDataFile;
    }

    public void setRawDataFiles(final List<File> rawDataFiles) {
        this.rawDataFiles = Collections.unmodifiableList(rawDataFiles);
    }

    public void setAvroDataFile(final File avroDataFile) {
        this.avroDataFile = avroDataFile;
    }
}
