package com.harrys.hyppo.source.api.task;

import com.harrys.hyppo.source.api.PersistingSemantics;
import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/17/15.
 */
public interface ProcessedDataPersister<T extends SpecificRecord> {
    void persistProcessedData(PersistProcessedData<T> operation) throws Exception;

    /**
     * The persister may specify that it guarantees idempotence, which may allow for persitence tasks to be retried
     * after a failure or restart.
     * @return The {@link PersistingSemantics} enum value appropriate for this persister implementation.
     */
    default PersistingSemantics retrySemantics(){
        return PersistingSemantics.Default;
    }
}
