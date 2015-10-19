package io.ingestion.source.api;

import io.ingestion.source.api.task.ProcessedDataFetcher;
import org.apache.avro.specific.SpecificRecord;

/**
 * Created by jpetty on 7/20/15.
 */
public abstract class ProcessedDataIntegration<T extends SpecificRecord> implements DataIntegration<T> {

    public ProcessedDataIntegration(){
        //  Default no-args constructor
    }


    public abstract ProcessedDataFetcher<T> newProcessedDataFetcher();

}
