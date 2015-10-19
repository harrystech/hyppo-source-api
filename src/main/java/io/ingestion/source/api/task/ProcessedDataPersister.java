package io.ingestion.source.api.task;

import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/17/15.
 */
@FunctionalInterface
public interface ProcessedDataPersister<T extends SpecificRecord> {
    void persistProcessedData(PersistProcessedData<T> operation) throws Exception;
}
