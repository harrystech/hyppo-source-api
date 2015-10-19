package io.ingestion.source.api;

import io.ingestion.source.api.task.RawDataFetcher;
import io.ingestion.source.api.task.RawDataProcessor;
import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/20/15.
 */
public abstract class RawDataIntegration<T extends SpecificRecord> implements DataIntegration<T> {

    public RawDataIntegration(){
        //   Default no args constructor
    }

    public abstract RawDataFetcher newRawDataFetcher();

    public abstract RawDataProcessor<T> newRawDataProcessor();

}
