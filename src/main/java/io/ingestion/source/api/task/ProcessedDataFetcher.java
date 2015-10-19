package io.ingestion.source.api.task;

import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/17/15.
 */
@FunctionalInterface
public interface ProcessedDataFetcher<T extends SpecificRecord> {
    void fetchProcessedData(FetchProcessedData<T> operation) throws Exception;
}
