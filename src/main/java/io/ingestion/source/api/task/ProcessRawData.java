package io.ingestion.source.api.task;

import io.ingestion.source.api.data.AvroRecordAppender;
import io.ingestion.source.api.model.DataIngestionJob;
import io.ingestion.source.api.model.DataIngestionTask;
import io.ingestion.source.api.model.IngestionSource;
import org.apache.avro.specific.SpecificRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

/**
 * Created by jpetty on 7/17/15.
 */
public final class ProcessRawData<T extends SpecificRecord> {

    private final DataIngestionTask task;

    private final File file;

    private final AvroRecordAppender<T> records;

    public ProcessRawData(final DataIngestionTask task, final File file, final AvroRecordAppender<T> records){
        this.task    = task;
        this.file    = file;
        this.records = records;
    }

    public final IngestionSource getSource(){
        return this.getJob().getIngestionSource();
    }

    public final DataIngestionJob getJob(){
        return this.task.getIngestionJob();
    }

    public final DataIngestionTask getTask(){
        return this.task;
    }

    public final InputStream openInputStream() throws IOException {
        return new GZIPInputStream(new FileInputStream(this.file));
    }

    public final void append(final T record) throws IOException {
        this.records.append(record);
    }

    public final void append(final Iterable<T> records) throws IOException {
        this.records.appendAll(records);
    }

    public final void append(final Iterator<T> records) throws IOException {
        this.records.appendAll(records);
    }
}
