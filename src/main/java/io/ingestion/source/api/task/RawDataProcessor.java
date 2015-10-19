package io.ingestion.source.api.task;

import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/17/15.
 */
@FunctionalInterface
public interface RawDataProcessor<T extends SpecificRecord> {
    void processRawData(ProcessRawData<T> operation) throws Exception;
}
