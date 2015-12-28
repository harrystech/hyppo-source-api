package com.harrys.hyppo.test;

import com.harrys.hyppo.source.api.data.AvroRecordAppender;
import com.harrys.hyppo.source.api.model.DataIngestionTask;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;

/**
 * Created by pettyjamesm on 12/28/15.
 */
public final class DataIngestionTaskWithAvroFile<T extends SpecificRecord> {

    private final DataIngestionTask task;

    private final AvroRecordAppender<T> appender;

    public DataIngestionTaskWithAvroFile(final DataIngestionTask task, final AvroRecordAppender<T> appender){
        this.task       = task;
        this.appender   = appender;
    }

    public final DataIngestionTask getTask() {
        return task;
    }

    public final AvroRecordAppender<T> getAvroAppender() {
        return appender;
    }
}
