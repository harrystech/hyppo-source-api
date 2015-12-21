package com.harrys.hyppo.source.api.task;

import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/17/15.
 */
public interface ProcessedDataFetcher<T extends SpecificRecord> {
    void fetchProcessedData(FetchProcessedData<T> operation) throws Exception;
}
