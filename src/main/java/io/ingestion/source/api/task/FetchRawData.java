package io.ingestion.source.api.task;

import io.ingestion.source.api.data.RawDataCollector;
import io.ingestion.source.api.model.DataIngestionJob;
import io.ingestion.source.api.model.DataIngestionTask;
import io.ingestion.source.api.model.IngestionSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by jpetty on 7/17/15.
 */
public final class FetchRawData {

    private final DataIngestionTask task;

    private final RawDataCollector accumulator;

    public FetchRawData(final DataIngestionTask task, final RawDataCollector accumulator) {
        this.task = task;
        this.accumulator = accumulator;
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

    public final void addData(final InputStream stream) throws IOException {
        this.accumulator.add(stream);
    }

    public final List<File> getDataFiles(){
        return this.accumulator.getRawFiles();
    }
}
