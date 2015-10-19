package io.ingestion.source.api.task;

import io.ingestion.source.api.data.AvroRecordAppender;
import io.ingestion.source.api.model.DataIngestionJob;
import io.ingestion.source.api.model.DataIngestionTask;
import io.ingestion.source.api.model.IngestionSource;
import org.apache.avro.specific.SpecificRecord;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by jpetty on 7/17/15.
 */
public final class FetchProcessedData<T extends SpecificRecord> {

    private final DataIngestionTask task;

    private final AvroRecordAppender<T> records;

    public FetchProcessedData(final DataIngestionTask task, final AvroRecordAppender<T> records){
        this.task       = task;
        this.records    = records;
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
